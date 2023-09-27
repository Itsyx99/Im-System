const path = require('path');
var webpack = require('webpack');

module.exports = {
    resolve: {
        extensions: ['.js', '.ts', '.json'],
    },
    devtool: 'source-map',// 打包出的js文件是否生成map文件（方便浏览器调试）
    mode: 'production',
    entry: {
        'lim-sdk': './src/lim.ts',
    },
    output: {
        filename: '[name].js',// 生成的fiename需要与package.json中的main一致
        path: path.resolve(__dirname, '../dist'),
        libraryTarget: process.env.platform === 'web' ? 'window' : 'umd',
        library: "imsdk"
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/i,
                use: [

                  {
                    loader: 'ts-loader',
                    options: {
                        // configFile: path.resolve(__dirname, './tslint.json'),
                        configFile: path.resolve(__dirname, '../tslint.json')
                    },
                  },

                  {
                    loader: 'js-conditional-compile-loader',
                    options: {
                      isDebug: process.env.NODE_ENV === 'development', // optional, this is default
                      WEBAPP: process.env.platform === 'web', // any name, used for /* IFTRUE_WEBAPP ...js code... FITRUE_WEBAPP */
                      WXAPP: process.env.platform === 'wx', // any name, used for /* IFTRUE_WXAPP ...js code... FITRUE_WXAPP */
                      RNAPP: process.env.platform === 'rn', // any name, used for /* IFTRUE_RNAPP ...js code... FITRUE_RNAPP */
                      UNIAPP: process.env.platform === 'uniapp', // any name, used for /* IFTRUE_UNIAPP ...js code... FITRUE_UNIAPP */
                    }
                  },

                ],
                exclude: /node_modules/
            }
        ],
    },
    plugins: [],
};

if (process.env.NODE_ENV === 'production') {
    module.exports.devtool = '#source-map'
    // http://vue-loader.vuejs.org/en/workflow/production.html
    module.exports.plugins = (module.exports.plugins || []).concat([
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: '"production"',
                // GIT_VERSION: `${JSON.stringify(gitRevision.version())}`,
                // GIT_COMMITHASH: `${JSON.stringify(gitRevision.commithash())}`,
                // GIT_BRANCH: `${JSON.stringify(gitRevision.branch())}`,
                PLATFORM: `${JSON.stringify(process.env.platform)}`
            }
        }),
        // new webpack.optimize.UglifyJsPlugin({
        //     sourceMap: true,
        //     compress: {
        //         warnings: false
        //     }
        // }),
        // new webpack.LoaderOptionsPlugin({
        //     minimize: true
        // })
    ])
}
