"use client";

import {usePathname, useRouter} from "next/navigation";
import {useEffect, useSyncExternalStore} from "react";
import {useLocalStorageValue} from "@/lib/use-local-storage";

const PUBLIC_ROUTES = new Set(["/", "/login"]);
const emptySubscribe = () => () => {
};

export default function AuthGate({children}: { children: React.ReactNode }) {
    const pathname = usePathname();
    const router = useRouter();
    const username = useLocalStorageValue("hypixel_username").trim();
    const hydrated = useSyncExternalStore(emptySubscribe, () => true, () => false);
    const isPublic = PUBLIC_ROUTES.has(pathname);

    useEffect(() => {
        if (hydrated && !isPublic && !username) {
            router.replace(`/login?redirect=${encodeURIComponent(pathname)}`);
        }
    }, [hydrated, isPublic, pathname, router, username]);

    return isPublic || (hydrated && username) ? children : null;
}
