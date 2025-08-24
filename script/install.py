import os
import sys
import time
import shutil
import requests
import subprocess
from rich.console import Console
from rich.panel import Panel
from rich.progress import Progress, SpinnerColumn, TextColumn, BarColumn, TaskProgressColumn
from rich.align import Align
from rich.text import Text
from rich.prompt import Confirm, Prompt
from rich.live import Live
from rich import box
import socket
import questionary
from pathlib import Path
from rich.columns import Columns
from rich.layout import Layout
from rich.table import Table
from rich.status import Status
import json
from rich.markdown import Markdown
from rich.syntax import Syntax

console = Console()

SPLASH_ART = """[bold cyan]
    __  __            _           __   _____ __        ____  __         __  
   / / / /_  ______  (_)  _____  / /  / ___// /____  _/ / / / /__  ____/ /__
  / /_/ / / / / __ \/ / |/_/ _ \/ /   \__ \/ //_/ / / / / / / _ \/ __  / _ \\
 / __  / /_/ / /_/ / />  </  __/ /   ___/ / ,< / /_/ / /_/ /  __/ /_/ /  __/
/_/ /_/\__, / .___/_/_/|_|\___/_/   /____/_/|_|\__, /\____/\___/\__,_/\___/ 
      /____/_/                                 /____/                         
[/bold cyan]"""

DIRECTORY_ART = """[yellow]
   📁 [bold]HypixelSkyBlock[/bold]
   ├── 📂 configuration
   │   ├── 📂 collections
   │   │   ├── 📄 regions.csv
   │   │   ├── 📄 fairysouls.csv
   │   │   └── 📄 crystals.csv
   │   ├── 📂 songs
   │   ├── 📂 items
   │   ├── 📂 SkyBlockPack
   │   └── 📂 pack_textures
   ├── 📂 plugins
   │   └── 📄 SkyBlockProxy.jar
   ├── 📄 SkyBlockCore.jar
   ├── 📄 NanoLimbo.jar
   ├── 📄 velocity.toml
   └── 📄 resources.json[/yellow]"""

LOADING_FRAMES = ["⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"]
CHECKMARK = "[green]✓[/green]"
PENDING = "[yellow]>[/yellow]"
WAITING = "[white]·[/white]"

def animate_text(text, speed=0.03):
    """Animate text appearing character by character"""
    rendered = ""
    for char in text:
        rendered += char
        console.print(Align.center(rendered), end="\r")
        time.sleep(speed)
    print()

def show_spinning_loader(text, duration=2):
    """Show a spinning loader with text"""
    with console.status(text, spinner="dots") as status:
        time.sleep(duration)

def animated_transition(old_content, new_content, duration=1.0):
    """Create a smooth transition between content"""
    steps = 10
    for i in range(steps + 1):
        console.clear()
        opacity = i / steps
        console.print(Align.center(f"\n\n{old_content}"))
        time.sleep(duration / steps)
    console.clear()
    console.print(Align.center(f"\n\n{new_content}"))

def display_directory_preview(path):
    """Show an animated directory structure preview"""
    console.clear()
    console.print(Align.center("[bold cyan]Directory Structure Preview[/bold cyan]"))
    console.print()

    for line in DIRECTORY_ART.split('\n'):
        console.print(Align.center(line))
        time.sleep(0.05)

    console.print()
    show_spinning_loader("[cyan]Preparing installation...[/cyan]", 2)

def show_error(message):
    """Display error message in a clearly visible panel"""
    console.print("\n")
    console.print(Panel(
        f"[bold red]{message}[/bold red]",
        title="Error",
        border_style="red",
        padding=(1, 2)
    ))
    console.print("\n")
    input("Press Enter to exit...")
    sys.exit(1)

class StylizedProgress:
    def __init__(self):
        self.steps = [
            "Checking Prerequisites",
            "Setting up Installation Directory",
            "Configuring Redis",
            "Configuring MongoDB",
            "Downloading Required Files",
            "Creating Server Scripts",
            "Finalizing Installation"
        ]
        self.current_step = 0
        self.layout = Layout()
        self.setup_layout()

    def setup_layout(self):
        """Initialize the layout with proper styling and content"""
        self.layout.split_column(
            Layout(name="header", size=8),
            Layout(name="body", size=12),
            Layout(name="footer", size=4)
        )

        # Style the panels properly
        self.layout["header"].update(Panel(
            Align.center(SPLASH_ART),
            border_style="cyan",
            box=box.DOUBLE,
            padding=(1, 0)
        ))

        # Initialize progress display
        self.update_progress("Initializing...")

    def update_progress(self, description=None):
        progress_table = Table.grid(padding=1, expand=True)
        for i, step in enumerate(self.steps):
            status = ""
            if i < self.current_step:
                status = "[green]✓[/green]"
            elif i == self.current_step:
                status = "[yellow]>[/yellow]"
            else:
                status = "[dim]·[/dim]"

            progress_table.add_row(
                status,
                f"[{'cyan' if i == self.current_step else 'white'}]{step}[/{'cyan' if i == self.current_step else 'white'}]"
            )

        self.layout["body"].update(Panel(
            progress_table,
            title="[bold blue]Installation Progress[/bold blue]",
            border_style="blue",
            box=box.ROUNDED
        ))

        # Update progress bar
        progress = (self.current_step / len(self.steps)) * 100
        bar_width = 50
        filled = int((progress / 100) * bar_width)
        bar = f"[{'=' * filled}{' ' * (bar_width - filled)}] {progress:.1f}%"

        self.layout["footer"].update(Panel(
            Align.center(f"{bar}\n{description or ''}"),
            border_style="cyan",
            box=box.ROUNDED
        ))

    def advance(self, description=None):
        """Advance to the next step"""
        if self.current_step < len(self.steps):
            self.current_step += 1
            self.update_progress(description)
            return True
        return False


def check_admin():
    try:
        is_admin = os.getuid() == 0
    except AttributeError:
        import ctypes
        is_admin = ctypes.windll.shell32.IsUserAnAdmin() != 0

    if not is_admin:
        show_error("Please run this script as Administrator!")
    return is_admin


def check_redis(host='localhost', port=6379):
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.settimeout(1)
        result = sock.connect_ex((host, port))
        sock.close()
        return result == 0
    except:
        return False


def download_file(url, destination, progress, task_id):
    response = requests.get(url, stream=True)
    total_size = int(response.headers.get('content-length', 0))

    with open(destination, 'wb') as file:
        if total_size == 0:
            file.write(response.content)
        else:
            downloaded = 0
            for data in response.iter_content(chunk_size=8192):
                downloaded += len(data)
                file.write(data)
                progress.update(task_id, completed=downloaded, total=total_size)


def create_server_scripts(install_path, server_type):
    script_content = f'''@echo off
title {server_type} Server Manager
set /p INSTANCES="How many {server_type} instances would you like to start? "
for /l %%i in (1,1,!INSTANCES!) do (
    start "{server_type} Server %%i" java --enable-preview -jar SkyBlockCore.jar {server_type}
    timeout /t 2
)
'''
    with open(install_path / f'start_{server_type.lower()}.bat', 'w') as f:
        f.write(script_content)


def create_start_all_script(install_path):
    content = '''@echo off
title SkyBlock Server Manager
start start_proxy.bat
timeout /t 5
start start_limbo.bat
timeout /t 2
echo Select which server types to start:
echo.
echo 1. ISLAND
echo 2. HUB
echo 3. THE_FARMING_ISLANDS
echo.
set /p SERVER_CHOICE="Enter server numbers (e.g., 1 2 3): "

for %%a in (%SERVER_CHOICE%) do (
    if "%%a"=="1" start start_island.bat
    if "%%a"=="2" start start_hub.bat
    if "%%a"=="3" start start_farming_islands.bat
)
'''
    with open(install_path / 'start_all.bat', 'w') as f:
        f.write(content)

def main():
    # Initialize progress tracker
    console = Console()
    progress = StylizedProgress()

    # Show splash screen with animation
    console.clear()
    animate_text(SPLASH_ART, speed=0.004)
    time.sleep(1)

    with Live(progress.layout, refresh_per_second=4, screen=True) as live:
        # Check prerequisites
        progress.update_progress("Checking system requirements...")
        if not check_admin():
            console.print(Panel("Please run this script as Administrator!",
                                title="Error",
                                border_style="red"))
            input("Press Enter to exit...")
            sys.exit(1)
        progress.advance("Prerequisites checked successfully!")

        # Directory setup
        console.clear()
        display_directory_preview(None)
        install_dir = questionary.path(
            "Where would you like to install HypixelSkyBlock?",
            default=str(Path.home() / "HypixelSkyBlock")
        ).ask()

        if install_dir is None:
            sys.exit(0)

        install_path = Path(install_dir)
        progress.advance("Installation directory set!")

        # Get installation directory
    console.clear()
    console.print("Installation Directory Setup")
    install_dir = questionary.path(
        "Where would you like to install HypixelSkyBlock?",
        default=str(Path.home() / "HypixelSkyBlock")
    ).ask()

    if install_dir is None:
        sys.exit(0)

    install_path = Path(install_dir)
    progress.advance()

    # Create directories
    directories = [
        'configuration/skyblock/collections',
        'configuration/skyblock/songs',
        'configuration/skyblock/items',
        'configuration/skyblock/SkyBlockPack',
        'configuration/skyblock/pack_textures',
        'plugins'
    ]

    with Progress() as progress:
        task = progress.add_task("Creating directories...", total=len(directories))
        for directory in directories:
            (install_path / directory).mkdir(parents=True, exist_ok=True)
            progress.advance(task)

    # Check Redis
    with console.status("Checking Redis connection...") as status:
        if not check_redis():
            console.print("Redis is not running! Please start Redis and try again.",
                          style="red")
            sys.exit(1)
        progress.advance()

    # Configuration
    console.clear()
    console.print("Server Configuration")

    config = {
        "mongodb": questionary.text("MongoDB URI:", default="mongodb://localhost").ask(),
        "redis-uri": questionary.text("Redis URI:", default="redis://localhost:6379").ask(),
        "velocity-secret": questionary.text("Velocity Secret (leave empty to auto-generate):").ask() or os.urandom(8).hex(),
        "host-name": questionary.text("Hostname:", default="0.0.0.0").ask(),
        "anticheat": questionary.confirm("Enable anticheat?", default=False).ask(),
        "limbo-port": "65535",
        "pterodactyl-mode": False,
        "spark": False,
        "require-authentication": False,
        "transfer-timeout": "800",
        "sandbox-mode": True
    }

    # Save configuration
    with open(install_path / 'configuration' / 'resources.json', 'w') as f:
        json.dump(config, f, indent=2)

    progress.advance()

    # Download files
    with Progress() as progress:
        download_task = progress.add_task("Downloading files...", total=100)

        # Download from repository
        base_url = "https://raw.githubusercontent.com/Swofty-Developments/HypixelSkyBlock/master/configuration"
        files_to_download = [
            ("Minestom.regions.csv", "configuration/collections/regions.csv"),
            ("Minestom.fairysouls.csv", "configuration/collections/fairysouls.csv"),
            ("Minestom.crystals.csv", "configuration/collections/crystals.csv"),
            ("NanoLimbo-1.9.1-all.jar", "NanoLimbo.jar"),
            ("velocity.toml", "velocity.toml")
        ]

        for filename, dest in files_to_download:
            download_file(f"{base_url}/{filename}",
                          install_path / dest,
                          progress,
                          download_task)
            progress.advance(download_task, 20)

        # Download latest releases
        releases_url = "https://api.github.com/repos/Swofty-Developments/HypixelSkyBlock/releases/latest"
        releases = requests.get(releases_url).json()

        for asset in releases['assets']:
            if 'SkyBlockProxy' in asset['name']:
                download_file(asset['browser_download_url'],
                              install_path / 'plugins' / 'SkyBlockProxy.jar',
                              progress,
                              download_task)
            elif 'SkyBlockCore' in asset['name']:
                download_file(asset['browser_download_url'],
                              install_path / 'SkyBlockCore.jar',
                              progress,
                              download_task)

    progress.advance()

    # Create server scripts
    with console.status("Creating server scripts...") as status:
        for server_type in ['ISLAND', 'HUB', 'THE_FARMING_ISLANDS']:
            create_server_scripts(install_path, server_type)
        create_start_all_script(install_path)

    progress.advance()

    # Final message
    console.clear()
    console.print(Panel(
        f"Installation Complete!\n\n"
        f"Installation Directory: {install_path}\n\n"
        "Next Steps:\n"
        "1. Navigate to the installation directory\n"
        "2. Run 'start_all.bat' to start all servers\n"
        "3. Select which server types to start\n"
        "4. For each server type, specify how many instances\n"
        "5. Connect to the server at " + config["host-name"] + ":25565\n\n"
                                                               "To get ADMIN permissions:\n"
                                                               "1. Log in and out of the server\n"
                                                               "2. Open MongoDB Compass\n"
                                                               "3. Go to Minestom -> data\n"
                                                               "4. Find your profile\n"
                                                               "5. Set your rank to \"ADMIN\"\n"
                                                               "6. Log back in",
        title="Installation Complete",
        border_style="green"
    ))

if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        console.print("\n[red]Installation cancelled by user.[/red]")
        sys.exit(1)
    except Exception as e:
        console.print(f"\n[red]An error occurred: {e}[/red]")
        sys.exit(1)