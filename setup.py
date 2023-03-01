from setuptools import setup, find_packages

setup(
    name="scrpng",
    version="1.0",
    packages=find_packages(),
    install_requires=[
        'scrapy',
        'attrs',
        'pathlib',
        'pydantic',
    ]
)
