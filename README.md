<img src="https://user-images.githubusercontent.com/114015/79403256-5bacee00-7f5c-11ea-8ce1-93e8128ac5d3.png" width="250px">

Evaluating Pareto-fronts generated by jMetal Framework

[![Build](https://github.com/thiagodnf/jmetrics/actions/workflows/build.yml/badge.svg)](https://github.com/thiagodnf/jmetrics/actions/workflows/build.yml)
[![GitHub Release](https://img.shields.io/github/release/thiagodnf/jmetrics.svg)](https://github.com/thiagodnf/jmetrics/releases/latest)
[![GitHub contributors](https://img.shields.io/github/contributors/thiagodnf/jmetrics.svg)](https://github.com/thiagodnf/jmetrics/graphs/contributors)
[![GitHub stars](https://img.shields.io/github/stars/thiagodnf/jmetrics.svg)](https://github.com/thiagodnf/jmetrics)
![GitHub top language](https://img.shields.io/github/languages/top/thiagodnf/jmetrics)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

## Download

[Click here](https://github.com/thiagodnf/jmetrics/releases) to download the latest version of this tool 

## Features

This tool supports the following features:
- Metrics
    - Hypervolume
    - HypervolumeApprox
    - Epsilon
    - GD
    - IGD
    - IGDPlus
    - Spread
    - ErrorRatio
- Generate an approximate pareto-front
- Export the results as .csv files

## Usage

To see all options, just type:

```sh
$ jmetrics --help
```

The output:

```bash
Usage: jmetrics [-h] [-s=<separator>] [-m=<metrics>...]... folder
Evaluating Pareto-fronts generated by jMetal Framework
      folder   folder that has the input files
  -h, --help   display this help and exit
  -m, --metrics=<metrics>...
               set the metrics to be calculated
               values: Hypervolume, HypervolumeApprox, Epsilon, GD, IGD,
                 IGDPlus, Spread, ErrorRatio, ALL
               default: [Hypervolume, IGD]
  -s, --separator=<separator>
               set the column separator
               values: Comma, SemiColon, Colon, Bar, Tab, Space
               default: Space
Copyright(c) 2020 jmetrics
```

For example:

```bash
$ jmetrics src/target-folder -s comma --metrics hypervolume igd
```

If there is no ```pareto-front.txt``` in this folder, the tool will generante an approximate pareto-front based on the files and save it into the same directory. If everything is ok, a ```result.csv``` is going to be generated.

If you want to see an example, please [click here](https://github.com/thiagodnf/jmetrics/tree/main/src/main/resources/examples/no-approx-pareto-front)

## Questions or Suggestions

Feel free to create <a href="https://github.com/thiagodnf/jmetrics/issues">issues</a> here as you need

## Contribute

Contributions to the this project are very welcome! We can't do this alone! Feel free to fork this project, work on it and then make a pull request.

## Authors

* **Thiago Ferreira** - *Initial work*

See also the list of [contributors](https://github.com/thiagodnf/jmetrics/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Donate

I open-source almost everything I can, and I try to reply to everyone needing help using these projects. Obviously, this takes time. You can integrate and use these projects in your applications for free! You can even change the source code and redistribute (even resell it).

However, if you get some profit from this or just want to encourage me to continue creating stuff, there are few ways you can do it:

<a href="https://www.buymeacoffee.com/thiagodnf" target="_blank">
  <img src="https://www.buymeacoffee.com/assets/img/guidelines/download-assets-sm-2.svg" alt="Buy Me A Coffee">
</a>
<br/>
<br/>
Thanks! ❤️
