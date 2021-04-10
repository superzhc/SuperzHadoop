package com.github.superzhc.flink.manage.constant;

/**
 * Flink脚本可用的action
 * @author superz
 * @create 2021/4/10 14:21
 */
public enum FlinkCLIAction {
    /**
     * Action "run" compiles and runs a program.
     */
    run
    /**
     * Action "run-application" runs an application in Application Mode.
     */
    ,run_application
    /**
     * Action "info" shows the optimized execution plan of the program (JSON).
     */
    ,info
    /**
     * Action "list" lists running and scheduled programs.
     */
    ,list
    /**
     * Action "stop" stops a running program with a savepoint (streaming jobs only).
     */
    ,stop
    /**
     * Action "cancel" cancels a running program.
     */
    ,cancel
    /**
     * Action "savepoint" triggers savepoints for a running job or disposes existing ones.
     */
    ,savepoint
}
