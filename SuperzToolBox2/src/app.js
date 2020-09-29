const Koa = require("koa");

// 注意require("koa-router")返回的是函数
const router = require("koa-router")();

// 处理body请求
const bodyParser = require("koa-bodyparser")

const app = new Koa();

app.use(async (ctx, next) => {
    const start = new Date().getTime();
    await next();
    const ms = new Date().getTime() - start;
    console.log(`${ctx.request.method} ${ctx.request.url}: ${ms}ms`);
});

// 要在router之前，不然request请求的参数不会被处理掉
app.use(bodyParser());

// 对于任何请求，app将调用该异步函数处理请求
// app.use(async (ctx, next) => {
//     await next();
//     // 设置response的Content-Type
//     ctx.response.type = "text/html";
//     // 设置response的内容
//     ctx.response.body = "<h1>Hello World!</h1>"
// });

// 路径路由
// router.get("/", async (ctx, next) => {
//     ctx.response.body = "<h1>Hello World</h1>";
// });
// router.get("/test/:name", async (ctx, next) => {
//     var name = ctx.params.name;
//     ctx.response.body = `<h1>Hello,${name}</h1>`;
// });
// router.post("/test/:text", async (ctx, next) => {
//     var text = ctx.params.text;
//     var params = ctx.request.body;
//     ctx.response.body = JSON.stringify(params);
// });
const controller = require("./controller");
app.use(controller());

//开启端口监听
app.listen(3000);
console.log("app started at post 3000...");