package com.github.superzhc.flink.manage.model.run;

import com.github.superzhc.flink.manage.model.FlinkCLIOptions;
import com.github.superzhc.flink.manage.annotation.CLIOption;
import lombok.Data;

/**
 * @author superz
 * @create 2021/4/10 14:26
 */
@Data
public abstract class FlinkRunCLIOptions extends FlinkCLIOptions {
    /**
     * -c,--class <classname>               Class with the program entry point
     *                                           ("main()" method). Only needed if the
     *                                           JAR file does not specify the class in
     *                                           its manifest.
     */
    @CLIOption(shortening = "c",name = "class",description = "指定进入的main方法，在Jar包的manifest中未指定的时候该参数必须存在")
    private String classname;
    /**
     * -C,--classpath <url>                 Adds a URL to each user code
     *                                           classloader  on all nodes in the
     *                                           cluster. The paths must specify a
     *                                           protocol (e.g. file://) and be
     *                                           accessible on all nodes (e.g. by means
     *                                           of a NFS share). You can use this
     *                                           option multiple times for specifying
     *                                           more than one URL. The protocol must
     *                                           be supported by the {@link
     *                                           java.net.URLClassLoader}.
     */
    @CLIOption(shortening = "C",name = "classpath")
    private String classpath;
    /**
     * 2021年4月9日 superz modify 默认为true，一般通过代码来运行的进程需要后台运行，不然代码运行完毕，命令也会随之结束
     * -d,--detached                        If present, runs the job in detached
     *                                           mode
     */
    @CLIOption(shortening = "d",name = "detached",description = "指定该参数使用后台运行模式")
    private Boolean detached=true;
    /**
     *-n,--allowNonRestoredState           Allow to skip savepoint state that
     *                                           cannot be restored. You need to allow
     *                                           this if you removed an operator from
     *                                           your program that was part of the
     *                                           program when the savepoint was
     *                                           triggered.
     */
    @CLIOption(shortening = "n",name = "allowNonRestoredState")
    private Boolean allowNonRestoredState;
    /**
     *-p,--parallelism <parallelism>       The parallelism with which to run the
     *                                           program. Optional flag to override the
     *                                           default value specified in the
     *                                           configuration.
     */
    @CLIOption(shortening = "p",name = "parallelism")
    private Integer parallelism;
    /**
     * -py,--python <pythonFile>            Python script with the program entry
     *                                           point. The dependent resources can be
     *                                           configured with the `--pyFiles`
     *                                           option.
     */
    @CLIOption(shortening = "py",name = "python")
    private String python;
    /**
     *-pyarch,--pyArchives <arg>           Add python archive files for job. The
     *                                           archive files will be extracted to the
     *                                           working directory of python UDF
     *                                           worker. Currently only zip-format is
     *                                           supported. For each archive file, a
     *                                           target directory be specified. If the
     *                                           target directory name is specified,
     *                                           the archive file will be extracted to
     *                                           a name can directory with the
     *                                           specified name. Otherwise, the archive
     *                                           file will be extracted to a directory
     *                                           with the same name of the archive
     *                                           file. The files uploaded via this
     *                                           option are accessible via relative
     *                                           path. '#' could be used as the
     *                                           separator of the archive file path and
     *                                           the target directory name. Comma (',')
     *                                           could be used as the separator to
     *                                           specify multiple archive files. This
     *                                           option can be used to upload the
     *                                           virtual environment, the data files
     *                                           used in Python UDF (e.g.: --pyArchives
     *                                           file:///tmp/py37.zip,file:///tmp/data.
     *                                           zip#data --pyExecutable
     *                                           py37.zip/py37/bin/python). The data
     *                                           files could be accessed in Python UDF,
     *                                           e.g.: f = open('data/data.txt', 'r').
     */
    @CLIOption(shortening = "pyarch",name = "pyArchives")
    private String pyArchives;
    /**
     *-pyexec,--pyExecutable <arg>         Specify the path of the python
     *                                           interpreter used to execute the python
     *                                           UDF worker (e.g.: --pyExecutable
     *                                           /usr/local/bin/python3). The python
     *                                           UDF worker depends on Python 3.5+,
     *                                           Apache Beam (version == 2.23.0), Pip
     *                                           (version >= 7.1.0) and SetupTools
     *                                           (version >= 37.0.0). Please ensure
     *                                           that the specified environment meets
     *                                           the above requirements.
     */
    @CLIOption(shortening = "pyexec",name = "pyExecutable")
    private String pyExecutable;
    /**
     * -pyfs,--pyFiles <pythonFiles>        Attach custom python files for job.
     *                                           The standard python resource file
     *                                           suffixes such as .py/.egg/.zip or
     *                                           directory are all supported. These
     *                                           files will be added to the PYTHONPATH
     *                                           of both the local client and the
     *                                           remote python UDF worker. Files
     *                                           suffixed with .zip will be extracted
     *                                           and added to PYTHONPATH. Comma (',')
     *                                           could be used as the separator to
     *                                           specify multiple files (e.g.:
     *                                           --pyFiles
     *                                           file:///tmp/myresource.zip,hdfs:///$na
     *                                           menode_address/myresource2.zip).
     */
    @CLIOption(shortening = "pyfs",name = "pyFiles")
    private String pyFiles;
    /**
     *-pym,--pyModule <pythonModule>       Python module with the program entry
     *                                           point. This option must be used in
     *                                           conjunction with `--pyFiles`.
     */
    @CLIOption(shortening = "pym",name = "pyModule")
    private String pyModule;
    /**
     *-pyreq,--pyRequirements <arg>        Specify a requirements.txt file which
     *                                           defines the third-party dependencies.
     *                                           These dependencies will be installed
     *                                           and added to the PYTHONPATH of the
     *                                           python UDF worker. A directory which
     *                                           contains the installation packages of
     *                                           these dependencies could be specified
     *                                           optionally. Use '#' as the separator
     *                                           if the optional parameter exists
     *                                           (e.g.: --pyRequirements
     *                                           file:///tmp/requirements.txt#file:///t
     *                                           mp/cached_dir).
     */
    @CLIOption(shortening = "pyreq",name = "pyRequirements")
    private String pyRequirements;
    /**
     *-s,--fromSavepoint <savepointPath>   Path to a savepoint to restore the job
     *                                           from (for example
     *                                           hdfs:///flink/savepoint-1537).
     */
    @CLIOption(shortening = "s",name = "fromSavepoint")
    private String fromSavepoint;
    /**
     *-sae,--shutdownOnAttachedExit        If the job is submitted in attached
     *                                           mode, perform a best-effort cluster
     *                                           shutdown when the CLI is terminated
     *                                           abruptly, e.g., in response to a user
     *                                           interrupt, such as typing Ctrl + C.
     */
    @CLIOption(shortening = "sae",name = "shutdownOnAttachedExit")
    private Boolean shutdownOnAttachedExit;
}
