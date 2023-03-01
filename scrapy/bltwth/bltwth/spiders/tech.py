import json
import re
from pathlib import Path

from scrapy.http import HtmlResponse

import scrapy
from ..utils import FileUtils


class TechSpider(scrapy.Spider):
    name = "tech"
    urlDict = json.loads(FileUtils.loadFileAsString(Path("categories.json")))

    def start_requests(self):
        for url, meta in self.urlDict.items():
            yield scrapy.Request(url=url.strip(), callback=self.parse)

    def parse(self, response: HtmlResponse, **kwargs):
        for tr in response.css("table.table-sm tr"):
            if tr.css("td:nth-child(1) a::text").get() is None:
                continue
            yield {
                "group": self.urlDict[response.url]['group'],
                "category": self.urlDict[response.url]['cat'],
                "tech": tr.css("td:nth-child(1) a::text").get().strip(),
                "sites": int(re.sub("\\D", "", tr.css("td:nth-child(2) a::text").get().strip())),
                "url": response.url
            }
