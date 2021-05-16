package com.ssm.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;
import com.ssm.model.Role;
import com.ssm.model.User;
import com.ssm.service.RoleService;
import com.ssm.service.UserService;
import com.ssm.utils.JedisPoolUtils;
import com.ssm.utils.PageSupport;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Eizooo
 * @date 2021/4/15 10:37
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private Logger logger = Logger.getLogger(UserController.class);
    @Resource(name = "userService")
    private UserService userService;
    //web.xml中使用listener在web容器启动时初始化spring容器,所以可以实现注解的依赖注入
    @Resource(name = "roleService")
    private RoleService roleService;
    /**
     * 通过SpringMVC创建的隐含的模型对象 来传递模型数据
     * @param username 不要求必须存在
     * @param model
     * @return Model
     */
    @RequestMapping("/welcome")
    public String welcome(@RequestParam(value = "username",required = false) String username, Model model){
        logger.info("你好,用户:"+username);
        model.addAttribute("username",username);
        //如果没有设置key,那么spring会用string来当作key
        //model.addAttribute(username);如果username是空值,那么这个语句就会报500的错误,所以不建议这么使用
        List<User> users = userService.getUserList();
        model.addAttribute("users",users);
        return "welcome";
    }

    /**
     * 通过SpringMVC创建的隐含的模型对象 来传递模型数据
     * @param username 要求必须存在,否则会报错
     * @param model 隐含对象,视图可以通过EL表达式获取其中内容
     * @return 逻辑视图名
     */
    @RequestMapping(value = "/welcome2",method = RequestMethod.GET,params = "username")
    public String welcome2(String username, Model model){
        logger.info("你好,用户:"+username);
        model.addAttribute("username",username);
        //如果没有设置key,那么spring会用string来当作key
        model.addAttribute(username);
        List<User> users = userService.getUserList();
        model.addAttribute("users",users);
        return "welcome";
    }

    /**
     * 通过ModelAndView对象返回模型数据
     * @param username username参数为必需
     * @return ModelAndView
     */
    @RequestMapping(value = "/welcome3",method = RequestMethod.GET,params = "username")
    public ModelAndView welcome3(String username){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome");
        List<User> users = userService.getUserList();
        modelAndView.addObject("users",users);
        modelAndView.addObject("username",username);
        return modelAndView;
    }

    @RequestMapping(value = "/dologin.html")
    public ModelAndView doLogin(User user, HttpSession session) throws Exception {
        User realUser = userService.getUser(user);
        ModelAndView modelAndView = new ModelAndView();
        if (realUser != null) {
            if (user.getUserPassword().equals(realUser.getUserPassword())) {
                modelAndView.setViewName("redirect:/user/main.html");
                session.setAttribute("userSession",realUser);
            }else {
                //用户名与密码不匹配,做局部异常处理
                throw new RuntimeException("用户密码输入不正确");
            }
        } else {
            //用户名不存在,做局部异常处理
            throw new RuntimeException("用户名不存在");
        }
        return modelAndView;
    }

    @RequestMapping("/main.html")
    public String main(HttpSession session){
        if(session.getAttribute("userSession") == null){
            return "redirect:/user/login.html";
        }
        return "frame";
    }

    @RequestMapping("/login.html")
    public String login(){
        return "login";
    }

//    /**
//     * 局部异常处理
//     * @param e 抓取到的异常
//     * @param request 用来携带异常内容的请求
//     * @return 登陆界面的逻辑视图名
//     */
//    @ExceptionHandler(value = {RuntimeException.class})
//    public String handlerException(RuntimeException e, HttpServletRequest request){
//        request.setAttribute("e",e);
//        return "login";
//    }

    @RequestMapping("/userlist.html")
    public String getUserList(Model model,@RequestParam(value = "queryname",required = false) String queryUserName,
                                          @RequestParam(value = "queryUserRole",required = false) String queryUserRole,
                                          @RequestParam(value = "pageIndex",required = false) String pageIndex){
        int realUserRole = 0;
        int pageSize = 5;
        int currentPageNo = 1;
        List<Role> roleList = null;
        List<User> userList = null;
        //对queryUserName进行非null处理,防止后面放入model中为null出错
        if(queryUserName == null){
            queryUserName = "";
        }
        //表单中拿到的是String类型的数据,对他进行非空和非null的判断
        if(queryUserRole != null && queryUserRole !=""){
            realUserRole = Integer.parseInt(queryUserRole);
        }
        //对pageIndex进行非空和非null的判断
        if(pageIndex != null && pageIndex != ""){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //拿到所有符合条件的数据总量
        int totalCount = userService.getUserCount(realUserRole, queryUserName);
        //利用工具类得到分页后的总页数
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo >totalPageCount){
            currentPageNo = totalPageCount;
        }
        //拿到用户分页后的用户列表数据
        userList = userService.getUserListRollPage(realUserRole,queryUserName,currentPageNo,pageSize);
        //拿到所有的用户角色信息
        roleList = roleService.getRoles();

        //往model中添加需要向view传递的信息
        model.addAttribute("userList",userList);
        model.addAttribute("roleList",roleList);
        model.addAttribute("totalPageCount",totalPageCount);
        model.addAttribute("queryUserName",queryUserName);
        model.addAttribute("queryUserRole",queryUserRole);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("currentPageNo",currentPageNo);

        return "userlist";
    }

    @RequestMapping("/userlistredis.html")
    public String getUserListByRedis(Model model,@RequestParam(value = "queryname",required = false) String queryUserName,
                              @RequestParam(value = "queryUserRole",required = false) String queryUserRole,
                              @RequestParam(value = "pageIndex",required = false) String pageIndex){
        int realUserRole = 0;
        int pageSize = 5;
        int currentPageNo = 1;
        List<Role> roleList = null;
        List<User> userList = null;

        Jedis jedis = JedisPoolUtils.getJedis();
        String userKey = "userList";
        String roleKey = "roleList";
        String userListJson = jedis.get(userKey);
        Gson gson = new Gson();
        //对queryUserName进行非null处理,防止后面放入model中为null出错
        if (queryUserName == null) {
            queryUserName = "";
        }
        //表单中拿到的是String类型的数据,对他进行非空和非null的判断
        if (queryUserRole != null && queryUserRole != "") {
            realUserRole = Integer.parseInt(queryUserRole);
        }
        //对pageIndex进行非空和非null的判断
        if (pageIndex != null && pageIndex != "") {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        if(userListJson == null) {
            //拿到所有符合条件的数据总量
            int totalCount = userService.getUserCount(realUserRole, queryUserName);
            //利用工具类得到分页后的总页数
            PageSupport pageSupport = new PageSupport();
            pageSupport.setPageSize(pageSize);
            pageSupport.setCurrentPageNo(currentPageNo);
            pageSupport.setTotalCount(totalCount);
            int totalPageCount = pageSupport.getTotalPageCount();
            //控制首页和尾页
            if (currentPageNo < 1) {
                currentPageNo = 1;
            } else if (currentPageNo > totalPageCount) {
                currentPageNo = totalPageCount;
            }
            //拿到用户分页后的用户列表数据
            userList = userService.getUserListRollPage(realUserRole, queryUserName, currentPageNo, pageSize);
            //拿到所有的用户角色信息
            roleList = roleService.getRoles();

            jedis.set(userKey,gson.toJson(userList));
            jedis.set(roleKey,gson.toJson(roleList));
            jedis.set("totalCount", String.valueOf(totalCount));
            jedis.set("totalPageCount", String.valueOf(totalPageCount));
        }
        //往model中添加需要向view传递的信息
        model.addAttribute("userList",gson.fromJson(jedis.get(userKey),new TypeToken<List<User>>() {}.getType()));
        model.addAttribute("roleList",gson.fromJson(jedis.get(roleKey),new TypeToken<List<Role>>() {}.getType()));
        model.addAttribute("totalPageCount",Integer.parseInt(jedis.get("totalPageCount")));
        model.addAttribute("queryUserName",queryUserName);
        model.addAttribute("queryUserRole",queryUserRole);
        model.addAttribute("totalCount",Integer.parseInt(jedis.get("totalCount")));
        model.addAttribute("currentPageNo",currentPageNo);

        jedis.close();
        return "userlist";
    }

    @RequestMapping(value = "/useradd.html",method = RequestMethod.GET)
    public String addUser(@ModelAttribute("user") User user){
        return "useradd";
    }

    @RequestMapping(value = "/useraddsave.html",method = RequestMethod.POST)
    public String addUserSave(User user, HttpSession session, HttpServletRequest request,
                              @RequestParam(value = "a_idPicPath", required = false)MultipartFile attach){
        //定义idPicPath用来接收传入user中的地址
        String idPicPath = null;
        //判断上传的文件是否为空
        System.out.println("1111");
        if(!attach.isEmpty()){
            System.out.println("22222");
            //服务器的全路径名+括号中的内容
            String path = request.getSession().getServletContext().getRealPath("statics"+ File.separator+"uploadfiles");
            //原文件名
            String oldFileName = attach.getOriginalFilename();
            //原文件名后缀
            String prefix = FilenameUtils.getExtension(oldFileName);
            int fileSize = 500000;
            if(attach.getSize() > fileSize){
                request.setAttribute("uploadFileError","* 上传文件大小不得超过500KB");
                return  "useradd";
            }else if(prefix.equalsIgnoreCase("jpg")
                        ||prefix.equalsIgnoreCase("png")
                        ||prefix.equalsIgnoreCase("jpeg")
                        ||prefix.equalsIgnoreCase("pneg")){
                //如果上传的图片格式正确
                String fileName = System.currentTimeMillis()+ RandomUtils.nextInt(0,1000000)+"_Personal.jpg";
                //两个参数分别是父路径目录和子路径目录或文件
                File targetFile = new File(path,fileName);
                //如果文件夹不存在,则创建此文件夹
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                //将上传的文件保存到服务器中
                try {
                    attach.transferTo(targetFile);
                }catch (Exception e){
                    e.printStackTrace();
                    request.setAttribute("uploadError","* 上传失败!");
                    return "useradd";
                }
                //拼接存入数据库中的文件地址
                idPicPath = "statics\\uploadfiles"+File.separator+fileName;
            }else {
                request.setAttribute("uploadFileError","* 上传图片格式不正确");
                return "useradd";
            }

        }
        user.setCreatedBy(((User)session.getAttribute("userSession")).getId());
        user.setCreationDate(new Date());
        user.setIdPicPath(idPicPath);
        if(userService.addUser(user)){
            return "redirect:/user/userlist.html";
        }
        return "useradd";
    }

    @RequestMapping(value = "/usermodify.html",method = RequestMethod.GET)
    public String getUserById(@RequestParam String uid,Model model){
        User user = userService.getUserById(uid);
        model.addAttribute("user",user);
        System.out.println(user.getBirthday());
        return "usermodify";
    }

    @RequestMapping(value = "/usermodifysave.html",method = RequestMethod.POST)
    public String modifySave(User user,HttpSession session){
        System.out.println(user);
        user.setModifyDate(new Date());
        user.setModifyBy(((User)session.getAttribute("userSession")).getId());
        //调用service中的方法进行数据持久化操作
        if(userService.modifyUser(user)){
            return "redirect:/user/userlist.html";
        }
        return "usermodify";
    }

    @RequestMapping(value = "/userview/{id}", method = RequestMethod.GET)
    public String userView(@PathVariable String id ,Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "userview";
    }

    @RequestMapping(value = "/userview2", method = RequestMethod.GET)
    @ResponseBody
    public Object userView(@RequestParam String id){
        if(null == id || "".equals(id)){
            return "nodata";
        }else {
            try {
                User user = userService.getUserById(id);
                System.out.println(user.getBirthday());
                return user;
            }catch (Exception e){
                e.printStackTrace();
                return "failed";
            }
        }

    }

    @RequestMapping(value = "ucexist.html")
    @ResponseBody
    public Object userCodeExist(@RequestParam String userCode){
        HashMap<String,String> existMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(userCode)){
            existMap.put("userCode","exist");
        }else {
            User user = userService.getUserCodeExist(userCode);
            if(user != null){
                existMap.put("userCode","exist");
            }else {
                existMap.put("userCode","noexist");
            }
        }
        Gson gson = new Gson();
        return gson.toJson(existMap);
    }

    @RequestMapping(value = "urlist")
    @ResponseBody
    public Object getUserRole(){
        List<Role> roles = null;
        roles = roleService.getRoles();
        Gson gson = new Gson();
        return gson.toJson(roles);
    }
}
