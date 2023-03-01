{:toc}

# CSS selectors

- :exclamation: [Visual examples](https://www.w3schools.com/cssref/trysel.php)

- [Complex selectors](https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector#complex_selectors), note that you can chain these things:

  ```
  document.querySelector("div.user-panel.main input[name='login']");
  ```

  

# Scrapy



## To-Do's

- [ ] !! https://scrapeops.io/python-scrapy-playbook/scrapy-beginners-guide/
- [ ] [Video course](https://www.youtube.com/watch?v=aIHTgF6polk&list=PLRzwgpycm-Fjvdf7RpmxnPMyJ80RecJjv) 



## CLI

Create new project:

```bash
scrapy startproject example
```

Create a spider:

```bash
scrapy genspider homepage example.com
```

Crawl:

```bash
scrapy crawl homepage	# name of spider
```

Output to JSON or CSV:

```
scrapy crawl bookstore -O output2.json
scrapy crawl bookstore -O output2.csv
```



## Shell

Start the shell:

```bash
scrapy shell "https://example.com"
```

Play around with elements:

```python
> response.css("title::text")	# get ::text
> allProducts = response.css("article.product_pod") # select all elements
> firstProduct = response.css("article.product_pod")[0] # select first element
```

You can even use control structures. Be aware of indentation. 

```
> for product in response.css("article.product_pod"):
>   print(product.css("h3 a")[0].attrib["title"])
>   print(product.css("h3 a")[0].attrib["href"])
>   print(product.css("p.price_color::text").get())
>   print("")
```



## Basic Example

Install and create a porject:

```
sudo apt-get install python3 python3-dev python3-pip libxml2-dev libxslt1-dev zlib1g-dev libffi-dev libssl-dev
pip install scrapy
scrapy startproject tutorial
```

Create spider `tutorial/spiders`:

```python
from pathlib import Path

import scrapy

# inherits from scrapy.Spider
class QuotesSpider(scrapy.Spider):
    name = "quotes"	# name of the spider
	
    # helper to get started, note the 'yield' instead of 'return'
    def start_requests(self):
        urls = [
            'https://quotes.toscrape.com/page/1/',
            'https://quotes.toscrape.com/page/2/',
        ]
        for url in urls:
            yield scrapy.Request(url=url, callback=self.parse)
	
    # what to parse upon response, can also do the 'callback=self.parse'
    def parse(self, response: HtmlResponse, **kwargs):
        page = response.url.split("/")[-2]
        filename = f'quotes-{page}.html'
        Path(filename).write_bytes(response.body)
        self.log(f'Saved file {filename}')
```

Or another one:

```python
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
```





# JavaScript





## Collect list of URLs (Vanilla)

From https://trends.builtwith.com/

Don't use `document.getElementsByClassName()` and such, use CSS selectors:

```bash
document.querySelectorAll()
document.querySelector()	# selects only first element
```

Example:

```javascript
results = []

panels = document.querySelectorAll(".panel-default")
Array.from(panels).forEach((panel) => {
	let category = panel.querySelector(".panel-title").innerText
	
    let subs = []
	let cats = panel.querySelectorAll(".si a")
	Array.from(cats).forEach((cat) => {
        subs.push({
            "name": cat.innerText,
            "url": "https://trends.builtwith.com" + cat.getAttribute("href")
        })
	});

	results.push({
	  "cat": category,
      "subs": subs
	})
});

console.log(results)
```
