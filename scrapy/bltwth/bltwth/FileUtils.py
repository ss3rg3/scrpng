from pathlib import Path


def loadFileAsString(path: Path) -> str:
    with open(path) as file:
        try:
            return file.read().strip()
        except Exception as e:
            raise e
