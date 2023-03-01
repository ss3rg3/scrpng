from setuptools import setup, find_packages

setup(
    name="scrpng",
    version="1.0",
    packages=find_packages(),
    py_modules=['scrpng'],
    install_requires=[
        'scrapy',
        'attrs',
        'pathlib',
        'pydantic',
    ]
)
