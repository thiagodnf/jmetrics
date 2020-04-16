
![logo](https://user-images.githubusercontent.com/114015/79403256-5bacee00-7f5c-11ea-8ce1-93e8128ac5d3.png)

Evaluate results generated by jMetal Framework

[![Build Status](https://travis-ci.org/thiagodnf/jmetrics.svg?branch=master)](https://travis-ci.org/thiagodnf/jmetrics)
![GitHub top language](https://img.shields.io/github/languages/top/thiagodnf/jmetrics)

# Usage

To see all options, just type:

```sh
$ jMetrics --help
```

The output:

```bash
Usage: jMetrics [-dh] [-r=<regex>] [-m=<metrics>...]... folder
Evaluate results generated by jMetal Framework
      folder            folder that has the input files
  -d, --debug           set the level for debugging one
  -h, --help            display this help and exit
  -m, --metrics=<metrics>...
                        values: Hypervolume, HypervolumeApprox, Epsilon, GD,
                          IGD, IGDPlus, Spread, ErrorRatio
                        Default: [IGD]
  -r, --regex=<regex>   set the column separator. Default: ,
Copyright(c) 2020 jMetrics
```

For example:

```bash
$ jMetrics src/target-folder --metrics hypervolume igd
```

If there is not pareto-front.txt in this folder, the tool will generante an approximate pareto-front based on the files and save this new front in the same directory. If everything is ok, a ```result.csv``` is going to be generated.
