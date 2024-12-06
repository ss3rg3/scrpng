import nodriver as uc
import asyncio

class Scraper:
    def __init__(self, browser, url):
        self.browser = browser  # This should be a fully awaited browser instance
        self.url = url
        self.main_tab = None

    async def run(self):
        # Ensure the browser instance is fully initialized
        self.main_tab = await self.browser.get("draft:,", new_tab=True)
        self.main_tab.add_handler(uc.cdp.fetch.RequestPaused, self.req_paused)
        self.main_tab.add_handler(
            uc.cdp.fetch.AuthRequired, self.auth_challenge_handler
        )
        await self.main_tab.send(uc.cdp.fetch.enable(handle_auth_requests=True))
        page = await self.main_tab.get(self.url)
        await self.main_tab.sleep(5)

    async def auth_challenge_handler(self, event: uc.cdp.fetch.AuthRequired):
        asyncio.create_task(
            self.main_tab.send(
                uc.cdp.fetch.continue_with_auth(
                    request_id=event.request_id,
                    auth_challenge_response=uc.cdp.fetch.AuthChallengeResponse(
                        response="ProvideCredentials",
                        username="USER",
                        password="PASS",
                    ),
                )
            )
        )

    async def req_paused(self, event: uc.cdp.fetch.RequestPaused):
        asyncio.create_task(
            self.main_tab.send(
                uc.cdp.fetch.continue_request(request_id=event.request_id)
            )
        )


async def main():
    """
    todo bad setup. `Scraper` should be tab and the constructor should set it up and then you have a method `get(url)`. So that you can reuse this thing
    """
    # Await the browser instance properly
    browser = await uc.start(
        browser_args=[f"--proxy-server=175.29.117.101:61234"],
    )
    url = "https://www.whatismyip.com/"
    scraper1 = Scraper(browser, url)
    url = "https://www.whatsmyip.org/"
    scraper2 = Scraper(browser, url)
    await scraper1.run()
    await scraper2.run()

if __name__ == "__main__":
    # Use asyncio.run() to ensure the event loop is properly managed
    asyncio.run(main())
