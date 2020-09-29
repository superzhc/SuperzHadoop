var get = async (ctx, next) => {
    var name = ctx.params.name;
    ctx.response.body = `<h1>Hello,${name}</h1>`;
};

var post = async (ctx, next) => {
    var text = ctx.params.text;
    var params = ctx.request.body;
    ctx.response.body = JSON.stringify(params);
}

module.exports = {
    'GET /test/:name': get,
    'POST /test/:text': post
};