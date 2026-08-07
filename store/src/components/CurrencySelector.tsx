"use client";

import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger,} from "@/components/ui/dropdown-menu";
import {setLocalStorageValue, useLocalStorageValue} from "@/lib/use-local-storage";

const currencies = ["AUD", "BRL", "CAD", "DKK", "EUR", "NOK", "NZD", "PLN", "GBP", "SEK", "USD"];

export default function CurrencySelector({footer = false}: { footer?: boolean }) {
    const currency = useLocalStorageValue("hypixel_currency", "USD");

    function selectCurrency(value: string) {
        setLocalStorageValue("hypixel_currency", value);
    }

    return (
        <DropdownMenu>
            <DropdownMenuTrigger
                className={footer ? "footer-currency-trigger" : "nav-button"}
                aria-label={`Currency: ${currency}`}
            >
                {currency} <span aria-hidden="true">▾</span>
            </DropdownMenuTrigger>
            <DropdownMenuContent align={footer ? "start" : "end"} className="currency-menu">
                {currencies.map((value) => (
                    <DropdownMenuItem
                        key={value}
                        className="currency-menu-item"
                        onSelect={() => selectCurrency(value)}
                    >
                        {value}
                    </DropdownMenuItem>
                ))}
            </DropdownMenuContent>
        </DropdownMenu>
    );
}
