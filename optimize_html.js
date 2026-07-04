const fs = require('fs');
const path = require('path');

// 获取命令行参数
const htmlPath = process.argv[2];

if (!htmlPath) {
    console.error('❌ 错误：请提供 HTML 文件路径');
    console.error('用法：node optimize_html.js <html-file>');
    process.exit(1);
}

if (!fs.existsSync(htmlPath)) {
    console.error(`❌ 错误：文件不存在 - ${htmlPath}`);
    process.exit(1);
}

console.log(`📝 正在优化：${htmlPath}`);
console.log('');

// 读取 HTML
let html = fs.readFileSync(htmlPath, 'utf-8');

// 1. 添加大屏样式
const bigScreenCSS = `
    <!-- 大屏优化（投影仪适配） -->
    <style>
        /* 基础大屏适配 */
        body { 
            font-size: 20px; 
            padding: 40px;
        }
        
        .header {
            padding: 40px;
            margin-bottom: 40px;
        }
        
        .header h1 { 
            font-size: 3em; 
            margin-bottom: 10px;
        }
        
        .header p { 
            font-size: 1.5em; 
        }
        
        .watch-section {
            padding: 40px;
            margin-bottom: 40px;
        }
        
        .watch-section h2 {
            font-size: 2em;
            margin-bottom: 30px;
        }
        
        .watch-btn { 
            padding: 30px 80px; 
            font-size: 2em; 
            font-weight: 600;
            border-radius: 16px;
        }
        
        .watch-section p {
            font-size: 1.2em;
            margin-top: 20px;
        }
        
        .schedule-section {
            padding: 40px;
        }
        
        .schedule-section h2 {
            font-size: 2em;
            margin-bottom: 30px;
        }
        
        .race-card { 
            padding: 30px; 
            margin-bottom: 25px;
            border-left-width: 8px;
        }
        
        .race-card .race-name { 
            font-size: 1.8em; 
            margin-bottom: 15px;
        }
        
        .race-card .race-location { 
            font-size: 1.3em; 
            margin-bottom: 15px;
        }
        
        .event-tag {
            padding: 10px 20px;
            font-size: 1.2em;
            border-radius: 8px;
            margin: 10px;
        }
        
        .countdown { 
            font-size: 1.3em; 
            margin-top: 20px;
        }
        
        .next-badge {
            padding: 8px 20px;
            font-size: 1em;
            border-radius: 8px;
        }
        
        /* 横屏优化 */
        @media (orientation: landscape) {
            .container { 
                max-width: 100%; 
                padding: 20px;
            }
            
            .race-card { 
                margin-bottom: 30px; 
            }
            
            .watch-btn {
                padding: 40px 100px;
                font-size: 2.5em;
            }
        }
        
        /* 超宽屏优化（投影仪常用） */
        @media (min-width: 1920px) {
            .container {
                max-width: 1800px;
            }
            
            body {
                font-size: 24px;
            }
        }
        
        /* 移除 hover 效果（投影仪无鼠标） */
        .watch-btn:hover {
            transform: none;
        }
        
        /* 增加对比度（投影仪可能亮度不足） */
        .race-card.next {
            box-shadow: 0 0 40px rgba(225,6,0,0.4);
        }
    </style>
`;

// 插入到 </head> 前
if (html.includes('</head>')) {
    html = html.replace('</head>', bigScreenCSS + '\n</head>');
    console.log('  ✅ 已添加大屏样式');
} else {
    console.log('  ⚠️  找不到 </head>，跳过样式注入');
}

// 2. 添加全屏支持（可选）
const fullscreenJS = `
    // 全屏支持（投影仪优化）
    function requestFullScreen() {
        const elem = document.documentElement;
        if (elem.requestFullscreen) {
            elem.requestFullscreen();
        } else if (elem.webkitRequestFullscreen) {
            elem.webkitRequestFullscreen();
        } else if (elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
        }
    }
    
    // 双击进入全屏
    document.addEventListener('dblclick', function() {
        requestFullScreen();
    });
    
    // 显示提示
    console.log('💡 提示：双击屏幕可以进入/退出全屏');
`;

// 插入到 </script> 前
if (html.includes('</script>')) {
    const lastScriptIndex = html.lastIndexOf('</script>');
    html = html.slice(0, lastScriptIndex) + '\n<script>\n' + fullscreenJS + '\n    </script>\n' + html.slice(lastScriptIndex);
    console.log('  ✅ 已添加全屏支持');
}

// 3. 优化视口（防止缩放）
const viewportMeta = '<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">';

if (html.includes('<meta name="viewport"')) {
    html = html.replace(/<meta name="viewport"[^>]+>/, viewportMeta);
    console.log('  ✅ 已优化视口（禁止缩放）');
}

// 写回文件
fs.writeFileSync(htmlPath, html, 'utf-8');

console.log('');
console.log('✅ HTML 优化完成！');
console.log('');
console.log('优化内容：');
console.log('  - 大屏样式（字体、按钮、间距）');
console.log('  - 横屏适配');
console.log('  - 超宽屏适配（1920px+）');
console.log('  - 全屏支持（双击切换）');
console.log('  - 禁止缩放（防止误操作）');
console.log('');
console.log(`文件已保存到：${htmlPath}`);
