from pathlib import Path

import scrapy


class FileUtils(scrapy.Item):

    @staticmethod
    def loadFileAsString(path: Path) -> str:
        print(path.absolute())
        with open(path) as file:
            try:
                return file.read().strip()
            except Exception as e:
                raise e
