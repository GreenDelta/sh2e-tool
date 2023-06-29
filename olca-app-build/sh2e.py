# This scipt packages the openLCA build as FCH-LCA tool. You run the
# normal openLCA build first (PDE export, package.py) and then this
# script. We only do this for Windows for now.

import datetime
import os
import shutil
from pathlib import Path


def main():
    print("package the FCH-LCA tool ...")
    build_dir = Path(__file__).parent / "build/win32.win32.x86_64"
    rename_artifacts(build_dir)
    t = datetime.datetime.now()
    pack = f"FCH-LCA_win64_{t.year}-{t.month:02d}-{t.day:02d}"
    shutil.make_archive(
        str((build_dir.parent / f"dist/{pack}").absolute()),
        "zip",
        str(build_dir),
    )


def rename_artifacts(build_dir: Path):
    app_dir = build_dir / "openLCA"
    if not app_dir.exists():
        print(f"error: app folder does not exist: {app_dir}")
        return
    exe = app_dir / "openLCA.exe"
    if exe.exists():
        os.rename(exe, app_dir / "FCH-LCA.exe")
    ini = app_dir / "openLCA.ini"
    if ini.exists():
        os.rename(ini, app_dir / "FCH-LCA.ini")
    os.rename(app_dir, build_dir / "FCH-LCA")


if __name__ == "__main__":
    main()
