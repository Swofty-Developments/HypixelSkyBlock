package installer

import (
	"context"
	"fmt"
	"io"
	"strings"
	"time"

	"github.com/moby/moby/client"
)

type ContainerStatus struct {
	ID       string
	Name     string
	State    string
	Health   string
	Restarts int
	Started  time.Time
}

func DockerClient() (*client.Client, error) {
	return client.New(client.FromEnv)
}

func ListProjectContainers(ctx context.Context) ([]ContainerStatus, error) {
	cli, err := DockerClient()
	if err != nil {
		return nil, err
	}
	defer cli.Close()

	list, err := cli.ContainerList(ctx, client.ContainerListOptions{All: true})
	if err != nil {
		return nil, err
	}
	statuses := make([]ContainerStatus, 0, len(list.Items))
	for _, c := range list.Items {
		name := strings.TrimPrefix(first(c.Names), "/")
		if !isProjectContainer(name) {
			continue
		}
		inspect, err := cli.ContainerInspect(ctx, c.ID, client.ContainerInspectOptions{})
		if err != nil {
			continue
		}
		container := inspect.Container
		state := "unknown"
		health := "n/a"
		if container.State != nil && container.State.Health != nil {
			health = string(container.State.Health.Status)
		}
		if container.State != nil {
			state = string(container.State.Status)
		}
		started := time.Time{}
		if container.State != nil && container.State.StartedAt != "" {
			started, _ = time.Parse(time.RFC3339Nano, container.State.StartedAt)
		}
		statuses = append(statuses, ContainerStatus{
			ID:       c.ID,
			Name:     name,
			State:    state,
			Health:   health,
			Restarts: container.RestartCount,
			Started:  started,
		})
	}
	return statuses, nil
}

func RestartContainer(ctx context.Context, name string) error {
	cli, err := DockerClient()
	if err != nil {
		return err
	}
	defer cli.Close()
	timeout := 20
	_, err = cli.ContainerRestart(ctx, name, client.ContainerRestartOptions{Timeout: &timeout})
	return err
}

func ContainerLogs(ctx context.Context, name string, tail string) (io.ReadCloser, error) {
	cli, err := DockerClient()
	if err != nil {
		return nil, err
	}
	return cli.ContainerLogs(ctx, name, client.ContainerLogsOptions{
		ShowStdout: true,
		ShowStderr: true,
		Follow:     true,
		Tail:       tail,
	})
}

func WaitHealthy(ctx context.Context, name string, timeout time.Duration) error {
	deadline, cancel := context.WithTimeout(ctx, timeout)
	defer cancel()

	cli, err := DockerClient()
	if err != nil {
		return err
	}
	defer cli.Close()

	ticker := time.NewTicker(2 * time.Second)
	defer ticker.Stop()

	for {
		inspect, err := cli.ContainerInspect(deadline, name, client.ContainerInspectOptions{})
		container := inspect.Container
		if err == nil && container.State != nil {
			if container.State.Health != nil {
				switch container.State.Health.Status {
				case "healthy":
					return nil
				case "unhealthy":
					return fmt.Errorf("%s is unhealthy", name)
				}
			}
			if container.State.Status == "exited" {
				return fmt.Errorf("%s exited", name)
			}
		}

		select {
		case <-deadline.Done():
			return fmt.Errorf("%s did not become healthy within %s", name, timeout)
		case <-ticker.C:
		}
	}
}

func MakeStaff(ctx context.Context, username string) error {
	username = strings.TrimSpace(username)
	if username == "" {
		return fmt.Errorf("username is required")
	}

	cli, err := DockerClient()
	if err != nil {
		return err
	}
	defer cli.Close()

	usernameLower := strings.ToLower(username)
	serializedUsernameLower := fmt.Sprintf("%q", usernameLower)
	serializedStaffRank := fmt.Sprintf("%q", "STAFF")
	script := fmt.Sprintf(
		`var result = db.profiles.updateOne({$or: [{ignLowercase: %q}, {ignLowercase: %q}]}, {$set: {rank: %q}}); if (result.matchedCount !== 1) { quit(44); }`,
		usernameLower,
		serializedUsernameLower,
		serializedStaffRank,
	)
	created, err := cli.ExecCreate(ctx, "hypixel_mongo", client.ExecCreateOptions{
		AttachStdout: false,
		AttachStderr: false,
		Cmd:          []string{"mongosh", "--quiet", "Minestom", "--eval", script},
	})
	if err != nil {
		return err
	}
	if _, err := cli.ExecStart(ctx, created.ID, client.ExecStartOptions{}); err != nil {
		return err
	}

	for {
		inspect, err := cli.ExecInspect(ctx, created.ID, client.ExecInspectOptions{})
		if err != nil {
			return err
		}
		if !inspect.Running {
			switch inspect.ExitCode {
			case 0:
				return nil
			case 44:
				return fmt.Errorf("player %q was not found; they need to join once first", username)
			default:
				return fmt.Errorf("mongosh exited with code %d", inspect.ExitCode)
			}
		}

		select {
		case <-ctx.Done():
			return ctx.Err()
		case <-time.After(250 * time.Millisecond):
		}
	}
}

func first(values []string) string {
	if len(values) == 0 {
		return ""
	}
	return values[0]
}

func isProjectContainer(name string) bool {
	return strings.HasPrefix(name, "hypixel_") ||
		strings.HasPrefix(name, "hypixelcore_") ||
		strings.HasPrefix(name, "service_") ||
		name == "pico_limbo" ||
		name == "game_server_builder"
}
