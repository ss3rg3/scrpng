import requests

# Define the URL you want to make a GET request to
url = 'https://httpbin.org/headers'

# Define custom headers
headers = {
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
    "Accept-Language": "en-US,en;q=0.9",
    "Referer": "https://www.google.com/",
    "Sec-Ch-Ua": "\"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Google Chrome\";v=\"116\"",
    "Sec-Ch-Ua-Mobile": "?0",
    "Sec-Ch-Ua-Platform": "Windows",
    "Sec-Fetch-Dest": "document",
    "Sec-Fetch-Mode": "navigate",
    "Sec-Fetch-Site": "cross-site",
    "Sec-Fetch-User": "?1",
    "Upgrade-Insecure-Requests": "1",
    "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36"
}

# Define the proxy dictionary
proxies = {
    'http': 'http://USER:PASS@175.29.117.101:61234',
    'https': 'http://USER:PASS@175.29.117.101:61234'
}

# Make the GET request with custom headers and proxy
response = requests.get(url, headers=headers)
# response = requests.get(url, headers=headers, proxies=proxies)


print(response.content.decode("utf-8"))
print(f"Status Code: {response.status_code}")
