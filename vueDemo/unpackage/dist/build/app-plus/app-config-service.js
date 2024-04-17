
  ;(function(){
  let u=void 0,isReady=false,onReadyCallbacks=[],isServiceReady=false,onServiceReadyCallbacks=[];
  const __uniConfig = {"pages":[],"globalStyle":{"navigationBar":{"backgroundColor":"#010101","type":"default","titleColor":"#ffffff"},"isNVue":false},"nvue":{"compiler":"uni-app","styleCompiler":"uni-app","flex-direction":"column"},"renderer":"auto","appname":"cinema","splashscreen":{"alwaysShowBeforeRender":true,"autoclose":true},"compilerVersion":"3.99","entryPagePath":"pages/index/index","entryPageQuery":"","realEntryPagePath":"","networkTimeout":{"request":60000,"connectSocket":60000,"uploadFile":60000,"downloadFile":60000},"tabBar":{"position":"bottom","color":"#999999","selectedColor":"#ffffff","borderStyle":"black","blurEffect":"none","fontSize":"10px","iconWidth":"24px","spacing":"3px","height":"50px","backgroundColor":"#010101","list":[{"pagePath":"pages/index/index","text":"首页","iconPath":"/static/ClarityHomeLine.png","selectedIconPath":"/static/ClarityHomeSolid.png"},{"pagePath":"pages/movieCinema/cinema","text":"电影","iconPath":"/static/PhFilmStrip.png","selectedIconPath":"/static/PhFilmStripFill.png"},{"pagePath":"pages/user/login","text":"我的","iconPath":"/static/OcticonPerson16.png","selectedIconPath":"/static/OcticonPersonFill16.png"}],"selectedIndex":0,"shown":true},"locales":{},"darkmode":false,"themeConfig":{}};
  const __uniRoutes = [{"path":"pages/index/index","meta":{"isQuit":true,"isEntry":true,"isTabBar":true,"tabBarIndex":0,"navigationBar":{"titleText":"","type":"default"},"isNVue":false}},{"path":"pages/user/login","meta":{"isQuit":true,"isTabBar":true,"tabBarIndex":2,"navigationBar":{"titleText":"","type":"default"},"isNVue":false}},{"path":"pages/user/info","meta":{"navigationBar":{"titleText":"个人信息","type":"default"},"isNVue":false}},{"path":"pages/movieCinema/cinema","meta":{"isQuit":true,"isTabBar":true,"tabBarIndex":1,"navigationBar":{"titleText":"","type":"default"},"isNVue":false}},{"path":"pages/user/updateName","meta":{"navigationBar":{"titleText":"修改名字","type":"default"},"isNVue":false}},{"path":"pages/user/updatePhone","meta":{"navigationBar":{"titleText":"修改电话","type":"default"},"isNVue":false}},{"path":"pages/user/updateProfile","meta":{"navigationBar":{"titleText":"修改简介","type":"default"},"isNVue":false}},{"path":"pages/movieCinema/movieInfo","meta":{"navigationBar":{"titleText":"电影","type":"default"},"isNVue":false}},{"path":"pages/test","meta":{"navigationBar":{"titleText":"测试","style":"custom","type":"default"},"isNVue":false}},{"path":"pages/movieCinema/detailInfo","meta":{"navigationBar":{"titleText":"详细信息","type":"default"},"isNVue":false}}].map(uniRoute=>(uniRoute.meta.route=uniRoute.path,__uniConfig.pages.push(uniRoute.path),uniRoute.path='/'+uniRoute.path,uniRoute));
  __uniConfig.styles=[];//styles
  __uniConfig.onReady=function(callback){if(__uniConfig.ready){callback()}else{onReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"ready",{get:function(){return isReady},set:function(val){isReady=val;if(!isReady){return}const callbacks=onReadyCallbacks.slice(0);onReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
  __uniConfig.onServiceReady=function(callback){if(__uniConfig.serviceReady){callback()}else{onServiceReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"serviceReady",{get:function(){return isServiceReady},set:function(val){isServiceReady=val;if(!isServiceReady){return}const callbacks=onServiceReadyCallbacks.slice(0);onServiceReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
  service.register("uni-app-config",{create(a,b,c){if(!__uniConfig.viewport){var d=b.weex.config.env.scale,e=b.weex.config.env.deviceWidth,f=Math.ceil(e/d);Object.assign(__uniConfig,{viewport:f,defaultFontSize:16})}return{instance:{__uniConfig:__uniConfig,__uniRoutes:__uniRoutes,global:u,window:u,document:u,frames:u,self:u,location:u,navigator:u,localStorage:u,history:u,Caches:u,screen:u,alert:u,confirm:u,prompt:u,fetch:u,XMLHttpRequest:u,WebSocket:u,webkit:u,print:u}}}}); 
  })();
  