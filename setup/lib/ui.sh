#!/usr/bin/env bash
# TUI utilities: colors, logging, drawing, gum wrappers, error handling

readonly CYAN='\033[0;36m'
readonly BOLD_CYAN='\033[1;36m'
readonly GREEN='\033[0;32m'
readonly YELLOW='\033[0;33m'
readonly RED='\033[0;31m'
readonly BOLD='\033[1m'
readonly DIM='\033[2m'
readonly RESET='\033[0m'

HAS_GUM=false

# ─── Stage tracking ──────────────────────────────────────────────────────────
CURRENT_STAGE="Initializing"
CURRENT_STEP=""

set_stage() { CURRENT_STAGE="$1"; CURRENT_STEP=""; }
set_step()  { CURRENT_STEP="$1"; }

on_error() {
    local exit_code=$?
    local line_no="${1:-unknown}"

    tput cnorm 2>/dev/null || true

    echo ""
    echo ""
    {
        echo -e "${RED}${BOLD}Installation Error${RESET}"
        echo ""
        echo -e "  Stage:     ${BOLD}${CURRENT_STAGE}${RESET}"
        [[ -n "$CURRENT_STEP" ]] && \
        echo -e "  Step:      ${CURRENT_STEP}"
        echo -e "  Exit code: ${exit_code}"
        echo -e "  Line:      ${line_no}"
        echo ""
        echo -e "  ${DIM}The installer encountered an unexpected error at the above stage.${RESET}"
        echo -e "  ${DIM}You can re-run the installer to try again.${RESET}"
        echo ""
        echo -e "  Troubleshooting:"
        echo -e "    - Check Docker is running:  ${CYAN}sudo systemctl status docker${RESET}"
        echo -e "    - Prune stale data:          ${CYAN}docker system prune -a --volumes${RESET}"
        echo -e "    - Check disk space:          ${CYAN}df -h${RESET}"
    } | draw_box "Error"
    echo ""
}

cleanup() { tput cnorm 2>/dev/null || true; }
trap cleanup EXIT

log_info()  { echo -e "${CYAN}[INFO]${RESET} $*"; }
log_ok()    { echo -e "${GREEN}[  ✓ ]${RESET} $*"; }
log_warn()  { echo -e "${YELLOW}[WARN]${RESET} $*"; }
log_err()   { echo -e "${RED}[FAIL]${RESET} $*"; }

die() { log_err "$@"; exit 1; }

clear_screen() { printf '\033[2J\033[H'; }

# ─── run_step: wraps a command with error catching ───────────────────────────
# Usage: run_step "Descriptive step name" command arg1 arg2 ...
# On failure: prints a styled error box with stage + step + output, then exits
run_step() {
    local step_name="$1"; shift
    CURRENT_STEP="$step_name"

    local output
    local rc=0
    output=$("$@" 2>&1) || rc=$?

    if [[ $rc -ne 0 ]]; then
        echo ""
        {
            echo -e "${RED}${BOLD}Failed: ${step_name}${RESET}"
            echo ""
            echo -e "  Stage:     ${BOLD}${CURRENT_STAGE}${RESET}"
            echo -e "  Exit code: ${rc}"
            echo ""
            if [[ -n "$output" ]]; then
                echo -e "  ${BOLD}Output (last 25 lines):${RESET}"
                echo "$output" | tail -25 | while IFS= read -r line; do
                    echo -e "    ${DIM}${line}${RESET}"
                done
            fi
            echo ""
            echo -e "  ${DIM}Fix the issue above and re-run the installer.${RESET}"
        } | draw_box "Error"
        echo ""
        exit 1
    fi

    CURRENT_STEP=""
    return 0
}

# Like run_step but streams output (for long-running commands like docker build).
# Captures to a log file for error reporting.
run_step_stream() {
    local step_name="$1"; shift
    local logfile="/tmp/hypixel-step-$$.log"
    CURRENT_STEP="$step_name"

    local rc=0
    "$@" 2>&1 | tee "$logfile" | while IFS= read -r line; do
        printf "\r\033[K  ${YELLOW}⠸${RESET} %s" "$(echo "$line" | tail -c 80)"
    done
    rc=${PIPESTATUS[0]}

    if [[ $rc -ne 0 ]]; then
        echo ""
        {
            echo -e "${RED}${BOLD}Failed: ${step_name}${RESET}"
            echo ""
            echo -e "  Stage:     ${BOLD}${CURRENT_STAGE}${RESET}"
            echo -e "  Exit code: ${rc}"
            echo ""
            if [[ -f "$logfile" ]]; then
                echo -e "  ${BOLD}Output (last 25 lines):${RESET}"
                tail -25 "$logfile" | while IFS= read -r line; do
                    echo -e "    ${DIM}${line}${RESET}"
                done
            fi
            echo ""
            echo -e "  ${DIM}Fix the issue above and re-run the installer.${RESET}"
        } | draw_box "Error"
        echo ""
        rm -f "$logfile"
        exit 1
    fi

    echo ""
    rm -f "$logfile"
    CURRENT_STEP=""
    return 0
}

center_text() {
    local text="$1"
    local cols
    cols=$(tput cols 2>/dev/null || echo 80)
    local stripped
    stripped=$(echo -e "$text" | sed 's/\x1b\[[0-9;]*m//g')
    local len=${#stripped}
    local pad=$(( (cols - len) / 2 ))
    (( pad < 0 )) && pad=0
    printf "%${pad}s" ""
    echo -e "$text"
}

draw_box() {
    local title="$1"; shift
    local width
    width=$(tput cols 2>/dev/null || echo 80)
    (( width > 100 )) && width=100
    local inner=$(( width - 4 ))

    echo -e "${CYAN}╔$(printf '═%.0s' $(seq 1 $((width-2))))╗${RESET}"
    if [[ -n "$title" ]]; then
        local pad=$(( (inner - ${#title}) / 2 ))
        (( pad < 0 )) && pad=0
        echo -e "${CYAN}║${RESET}$(printf ' %.0s' $(seq 1 $pad))${BOLD_CYAN}${title}${RESET}$(printf ' %.0s' $(seq 1 $((inner - pad - ${#title}))))  ${CYAN}║${RESET}"
        echo -e "${CYAN}╠$(printf '═%.0s' $(seq 1 $((width-2))))╣${RESET}"
    fi
    while IFS= read -r line || [[ -n "$line" ]]; do
        local stripped_line
        stripped_line=$(echo -e "$line" | sed 's/\x1b\[[0-9;]*m//g')
        local line_len=${#stripped_line}
        local right_pad=$(( inner - line_len ))
        (( right_pad < 0 )) && right_pad=0
        echo -e "${CYAN}║${RESET} ${line}$(printf ' %.0s' $(seq 1 $right_pad)) ${CYAN}║${RESET}"
    done
    echo -e "${CYAN}╚$(printf '═%.0s' $(seq 1 $((width-2))))╝${RESET}"
}

spinner() {
    local pid=$1 msg="$2"
    local frames=("⠋" "⠙" "⠹" "⠸" "⠼" "⠴" "⠦" "⠧" "⠇" "⠏")
    local i=0
    tput civis 2>/dev/null || true
    while kill -0 "$pid" 2>/dev/null; do
        printf "\r  ${YELLOW}%s${RESET} %s" "${frames[$i]}" "$msg"
        i=$(( (i+1) % ${#frames[@]} ))
        sleep 0.1
    done
    wait "$pid" 2>/dev/null
    local rc=$?
    printf "\r\033[K"
    tput cnorm 2>/dev/null || true
    return $rc
}

# ─── gum wrappers (fallback to plain prompts) ────────────────────────────────
gum_style() {
    if $HAS_GUM; then
        gum style "$@"
    else
        shift
        local text=""
        for arg in "$@"; do
            case "$arg" in --*) ;; *) text="$arg" ;; esac
        done
        echo -e "${BOLD_CYAN}${text}${RESET}"
    fi
}

gum_input() {
    if $HAS_GUM; then
        gum input "$@"
    else
        local prompt="" default=""
        while [[ $# -gt 0 ]]; do
            case "$1" in
                --placeholder) shift; default="$1" ;;
                --header) shift; prompt="$1" ;;
                --value) shift; default="$1" ;;
                *) ;;
            esac
            shift
        done
        echo -e "${CYAN}${prompt}${RESET} [${DIM}${default}${RESET}]: " >&2
        local val
        read -r val
        echo "${val:-$default}"
    fi
}

gum_confirm() {
    if $HAS_GUM; then
        gum confirm "$@"
    else
        local prompt="$1"
        echo -en "${CYAN}${prompt}${RESET} [Y/n]: " >&2
        local ans
        read -r ans
        [[ -z "$ans" || "$ans" =~ ^[Yy] ]]
    fi
}

gum_choose() {
    if $HAS_GUM; then
        gum choose "$@"
    else
        local items=()
        local header=""
        while [[ $# -gt 0 ]]; do
            case "$1" in
                --header) shift; header="$1" ;;
                --height|--cursor|--cursor.foreground|--selected.foreground) shift ;;
                *) items+=("$1") ;;
            esac
            shift
        done
        [[ -n "$header" ]] && echo -e "${CYAN}${header}${RESET}" >&2
        for i in "${!items[@]}"; do
            echo "  $((i+1))) ${items[$i]}" >&2
        done
        echo -n "  Choice: " >&2
        local choice
        read -r choice
        if [[ "$choice" =~ ^[0-9]+$ ]] && (( choice >= 1 && choice <= ${#items[@]} )); then
            echo "${items[$((choice-1))]}"
        fi
    fi
}

gum_filter_multi() {
    local header="$1"; shift
    local selected_csv="$1"; shift
    local items=("$@")

    if $HAS_GUM; then
        local args=(--no-limit --header "$header" --height 20
                    --indicator "●" --unselected-prefix "○ " --selected-prefix "● "
                    --header.foreground "6" --match.foreground "2")
        IFS=',' read -ra pre_selected <<< "$selected_csv"
        for s in "${pre_selected[@]}"; do
            args+=(--selected "$s")
        done
        printf '%s\n' "${items[@]}" | gum filter "${args[@]}"
    else
        echo -e "\n${CYAN}${header}${RESET}" >&2
        echo -e "${DIM}Enter numbers separated by spaces, or press Enter for defaults${RESET}" >&2

        IFS=',' read -ra pre_selected <<< "$selected_csv"
        declare -A is_selected
        for s in "${pre_selected[@]}"; do
            is_selected["$s"]=1
        done

        for i in "${!items[@]}"; do
            local marker="  "
            [[ -n "${is_selected[${items[$i]}]+x}" ]] && marker="${GREEN}*${RESET} "
            echo -e "  $((i+1))) ${marker}${items[$i]}" >&2
        done

        echo -n "  Selection (e.g. 1 2 3): " >&2
        local choices
        read -r choices

        if [[ -z "$choices" ]]; then
            for s in "${pre_selected[@]}"; do echo "$s"; done
        else
            for c in $choices; do
                (( c >= 1 && c <= ${#items[@]} )) && echo "${items[$((c-1))]}"
            done
        fi
    fi
}

gum_spin() {
    local title="$1"; shift
    if $HAS_GUM; then
        gum spin --spinner dot --title "$title" --spinner.foreground "3" -- "$@"
    else
        "$@" &
        spinner $! "$title"
    fi
}
