import time
import logging

logging.basicConfig(level=10)

from selenium.common.exceptions import WebDriverException
from selenium.webdriver.remote.webdriver import By
import selenium.webdriver.support.expected_conditions as EC  # noqa
from selenium.webdriver.support.wait import WebDriverWait
import undetected_chromedriver as uc

driver = uc.Chrome(version_main=113)
# driver.get("https://www.4chan.org")
driver.get("https://www.cyberport.de")
print(driver.page_source)

time.sleep(20)
driver.quit()
