"use client";

import {useSyncExternalStore} from "react";

const LOCAL_STORAGE_EVENT = "hypixel-local-storage";

function subscribe(callback: () => void) {
    window.addEventListener("storage", callback);
    window.addEventListener(LOCAL_STORAGE_EVENT, callback);
    return () => {
        window.removeEventListener("storage", callback);
        window.removeEventListener(LOCAL_STORAGE_EVENT, callback);
    };
}

export function useLocalStorageValue(key: string, fallback = "") {
    return useSyncExternalStore(
        subscribe,
        () => localStorage.getItem(key) || fallback,
        () => fallback,
    );
}

export function setLocalStorageValue(key: string, value: string) {
    localStorage.setItem(key, value);
    window.dispatchEvent(new Event(LOCAL_STORAGE_EVENT));
}
