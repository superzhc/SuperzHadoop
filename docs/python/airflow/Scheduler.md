# Scheduler

任务调度服务，周期性地轮询任务的调度计划，以确定是否触发任务执行，根据 dags 生成任务，并提交到消息中间件队列中 (redis 或 rabbitMq)