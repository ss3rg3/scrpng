import nodriver as uc


async def main():
    browser = await uc.start()
    page1 = await browser.get('https://www.conrad.de/')
    page2 = await browser.get('https://www.otto.de/')

    await browser.sleep(10)



if __name__ == '__main__':

    uc.loop().run_until_complete(main())
