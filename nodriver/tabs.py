import nodriver as uc
from bs4 import BeautifulSoup

async def main():
    browser = await uc.start()
    page1 = await browser.get('https://www.conrad.de/', new_tab=True)
    page2 = await browser.get('https://www.otto.de/', new_tab=True)

    source1 = await page1.get_content()
    source2 = await page2.get_content()

    print(BeautifulSoup(source1, 'html.parser').title)
    print(BeautifulSoup(source2, 'html.parser').title)

    await browser.sleep(10)
    await page2.close()
    await browser.sleep(10)



if __name__ == '__main__':

    uc.loop().run_until_complete(main())
