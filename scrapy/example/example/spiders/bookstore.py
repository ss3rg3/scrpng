import scrapy
from scrapy.http import HtmlResponse


class BookStoreSpider(scrapy.Spider):
    name = "bookstore"
    start_urls = ["https://books.toscrape.com/"]

    baseUrl = "https://books.toscrape.com"

    def parse(self, response: HtmlResponse, **kwargs):
        for product in response.css("article.product_pod"):
            yield {
                'title': product.css("h3 a")[0].attrib["title"],
                'url': f"{self.baseUrl}/{product.css('h3 a')[0].attrib['href']}",
                'price': product.css("p.price_color::text").get()
            }
        nextPage = response.css("li.next a").attrib["href"]
        if nextPage is not None:
            if not nextPage.startswith("catalogue"):
                nextPage = f"catalogue/{nextPage}"
            yield response.follow(f"{self.baseUrl}/{nextPage}")
