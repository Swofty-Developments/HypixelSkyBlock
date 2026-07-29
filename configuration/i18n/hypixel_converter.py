import os
import re

format = {
    "black": "black",
    "dark_blue": "dark_blue",
    "dark_green": "dark_green",
    "dark_aqua": "dark_aqua",
    "dark_red": "dark_red",
    "dark_purple": "dark_purple",
    "gold": "gold",
    "gray": "gray",
    "dark_gray": "dark_gray",
    "blue": "blue",
    "green": "green",
    "aqua": "aqua",
    "red": "red",
    "light_purple": "light_purple",
    "yellow": "yellow",
    "white": "white",
    "bold": "bold",
    "italic": "italic",
    "underlined": "underlined",
    "strikethrough": "strikethrough",
    "obfuscated": "obfuscated",
    "reset": "reset"
}


def convert_line(line):
    def replacer(match):
        code = match.group(1)

        if code in format:
            return f"<{format[code]}>"
        else:
            return match.group(0)

    return re.sub(r"%%(.*?)%%", replacer, line)


def main():
    file_path = input("relative file path: ").strip()

    if not os.path.isfile(file_path):
        print("could not find file")
        return

    with open(file_path, "r", encoding="utf-8") as f:
        lines = f.readlines()

    converted_lines = [convert_line(line) for line in lines]
    with open(file_path, "w", encoding="utf-8") as f:
        f.writelines(converted_lines)

    print("converted the file")


if __name__ == "__main__":
    main()
