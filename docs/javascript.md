{:toc}

# Vanilla



## Collect list of URLs

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

