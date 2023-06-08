# sh2e-tool

This repository contains the source code of _sh2e-tool_ developed in the
[SH2E project](https://sh2e.eu/project/). The tool is based on openLCA.
For any information regarding building the tool from source, see the
[repository of the openLCA application](https://github.com/GreenDelta/olca-app).

In order to include the latest upstream changes of openLCA into the tool,
you can do the following:

```bash
# add the openLCA repository as 'upstream' repository
git remote add upstream https://github.com/GreenDelta/olca-app.git

# fetch updates and set the sh2e changes on top of them
git fetch upstream
git rebase upstream/master
```
