<script setup>
import { ref, onMounted } from "vue";

const contributors = ref([]);
const loading = ref(true);
const error = ref(null);

onMounted(async () => {
  try {
    const response = await fetch(
      "https://api.github.com/repos/Swofty-Developments/HypixelSkyblock/contributors",
    );
    if (!response.ok) throw new Error(`GitHub API error: ${response.status}`);
    contributors.value = await response.json();
  } catch (err) {
    error.value = err.message;
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div class="contributors-wrapper">
    <div v-if="loading" class="status-text">
      <span class="pulse-dot"></span> Fetching contributors...
    </div>
    <div v-else-if="error" class="status-text error">⚠ {{ error }}</div>

    <div v-else class="contributors">
      <a
        v-for="(user, i) in contributors"
        :key="user.id"
        :href="user.html_url"
        target="_blank"
        rel="noopener"
        class="contributor"
        :title="`${user.login} · ${user.contributions} contributions`"
        :style="{ '--delay': `${i * 30}ms` }"
      >
        <div class="avatar-ring">
          <img :src="user.avatar_url" :alt="user.login" />
        </div>
        <span class="username">{{ user.login }}</span>
      </a>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Rajdhani:wght@500;600&family=JetBrains+Mono:wght@400;500&display=swap");

.contributors-wrapper {
  margin-top: 20px;
}

.status-text {
  font-family: "JetBrains Mono", monospace;
  font-size: 0.75rem;
  color: var(--vp-c-text-2, #888);
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-text.error {
  color: #f87171;
}

.pulse-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #4ade80;
  animation: pulse 1.4s ease-in-out infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.4;
    transform: scale(0.7);
  }
}

.contributors {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.contributor {
  position: relative;
  display: flex;
  align-items: center;
  gap: 7px;
  padding: 5px 10px 5px 5px;
  border-radius: 6px;
  text-decoration: none;
  color: inherit;
  background: var(--vp-c-bg-soft, rgba(255, 255, 255, 0.04));
  border: 1px solid var(--vp-c-divider, rgba(255, 255, 255, 0.08));
  transition:
    background 0.18s,
    border-color 0.18s,
    transform 0.18s;
  animation: fadeIn 0.35s ease both;
  animation-delay: var(--delay);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(6px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.contributor:hover {
  background: var(--vp-c-bg-mute, rgba(255, 255, 255, 0.08));
  border-color: var(--vp-c-brand-1, #4ade80);
  transform: translateY(-2px);
}

.avatar-ring {
  position: relative;
  width: 26px;
  height: 26px;
  border-radius: 50%;
  padding: 1.5px;
  background: linear-gradient(
    135deg,
    var(--vp-c-brand-1, #4ade80),
    var(--vp-c-brand-2, #22d3ee)
  );
  flex-shrink: 0;
}

.contributor:hover .avatar-ring {
  background: linear-gradient(135deg, #f9a825, #f06292);
}

.avatar-ring img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  display: block;
  object-fit: cover;
  border: 1.5px solid var(--vp-c-bg, #1a1a1a);
}

.username {
  font-family: "Rajdhani", sans-serif;
  font-weight: 600;
  font-size: 0.78rem;
  letter-spacing: 0.02em;
  color: var(--vp-c-text-1, #eee);
  white-space: nowrap;
  line-height: 1;
}
</style>
