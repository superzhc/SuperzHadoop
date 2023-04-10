import setuptools

setuptools.setup(
    # TODO:读取pom文件的配置
    name="superz-python",
    version="0.3.0",
    author="superz",
    description="",
    classifiers=[
        "Development Status :: 3 - Alpha",
        "Intended Audience :: Developers",
        "License :: OSI Approved :: Apache Software License",
        "Natural Language :: Chinese (Simplified)",
        "Programming Language :: Python :: 3.7"
    ],
    packages=setuptools.find_namespace_packages(where="./src"),
    python_requires=">=3.7"
)
