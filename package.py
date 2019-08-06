import os

"""
Using 7-Zip, packages the extension as an .xpi file
in the dist/ directory.
"""

def join(path, name): return f"{path}/{name}"

path = "build/kotlin-js-min/main"
files = ("kotlin.js", "condor2extension.js")

target = "dist"
manifest = "manifest.json"

# The paths to all files which will be added to the .xpi file.
paths = (manifest, *[join(path, f) for f in files])

zip_name = "condor2extension.zip"
if zip_name in os.listdir():
    os.remove(zip_name)

cmd = f'7z a -tzip {zip_name} {" ".join(paths)}'
print(f"Running command: {cmd}")
os.system(cmd)

if not os.path.exists(target):
    os.mkdir(target)

xpi = zip_name.replace(".zip", ".xpi")
xpi_path = join(target, xpi)

if xpi in os.listdir(target):
    os.remove(xpi_path)

os.rename(zip_name, xpi_path)
print(f"\nCreated {xpi_path}")