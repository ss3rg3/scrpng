Note that you need the following:

1. Get the Chromium binaries (Note that CHROME is only available as `deb` package, but works as well)
   - Get the "Branch Base Position" from your desired version from [here](https://chromiumdash.appspot.com/releases?platform=Windows)
   - Get the download from [here](https://commondatastorage.googleapis.com/chromium-browser-snapshots/index.html) based on your OS, e.g. "Linux_x64" ("Linux" is old)
   - For details see [StackOverflow](https://superuser.com/questions/203606/where-can-i-get-chromium-binaries)
2. An edited ChromeDriver (names of `cdc_*` variables replaced)
   - Downloaded with Chromium, see above
3. `ublock` extension as `.crx` file
4. You also need to configure the Chromium profile to make `ublock` work (first startup downloads the lists and you need to enable blocking of pop-ups and annoyances)