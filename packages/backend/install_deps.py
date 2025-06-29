import subprocess
with open("requirements.txt", "r", encoding="utf-8") as f:
    packages = f.readlines()
    for pkg in packages:
        subprocess.run([".\\venv\\Scripts\\python", "-m", "pip", "install", pkg.strip()])