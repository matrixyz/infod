<!DOCTYPE html>
<html lang="en">
<div th:replace="head :: copy"></div>

<body class="no-skin">

<div th:replace="navigation :: copy"></div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">


    <div th:replace="sidebar :: copy"></div>

    <!-- /section:basics/sidebar -->
    <div class="main-content">
        <!-- #section:basics/content.breadcrumbs -->
        <div class="breadcrumbs" id="breadcrumbs">
            <ul class="breadcrumb">
                <li th:each="item,itemStat: ${r'${'}breadNavi}">
                    <a th:href="${r'${'}item.rgtUrl}" >
                        <i th:class="${r'${'}item.rgtId}==0 ?'icon wb-home':''" th:text="${r'${'}item.rgtText}"></i>
                    </a>
                </li>
            </ul><!-- /.breadcrumb -->



            <!-- /section:basics/content.searchbox -->
        </div>

        <div class="page-content">
            <!-- /section:settings.box -->
            <div class="page-content-area">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->

                        <div class="row">
                            <div class="col-xs-12">
                                <!-- search form title start-->
                                <div class="table-header">
                                    管理
                                </div>
                                <div class="space-4"> </div>
                                <!-- 查询输入表单开始-->
                                <form id="form-${className}"   role="form" action="/${className}/list" METHOD="get">
                                    <div class="row">
                                        <label class="col-sm-1 control-label no-padding-right" for="custAddr"> 客户姓名：</label>
                                        <div class="col-sm-3">
                                            <input   type="text" id="custAddr" name="custAddr" placeholder="客户姓名" class="col-sm-12"  th:value="${r'${'}queryParams.custAddr}" maxlength="50"/>
                                        </div>
                                        <label class="col-sm-1 control-label no-padding-right" for="beginDate"> 添加时间：</label>
                                        <div class="col-sm-3">
                                            <input readonly="readonly"  type="text" id="beginDate" name="beginDate" placeholder="客户添加时间开始" class="col-sm-6 "  th:value="${r'${'}queryParams.beginDate==null?'':#dates.format(queryParams.beginDate,'yyyy-MM-dd HH:mm:ss')}" />
                                            <input  readonly="readonly" type="text" id="endDate" name="endDate" placeholder="客户添加时间结束" class="col-sm-6 "  th:value="${r'${'}queryParams.endDate==null?'':#dates.format(queryParams.endDate,'yyyy-MM-dd HH:mm:ss')}" />
                                        </div>
                                        <label class="col-sm-1 control-label no-padding-right" for="custManagerId"  > 客户经理：</label>
                                        <div class="col-sm-3">
                                            <select class="col-sm-12" id="custManagerId" name="custManagerId">
                                                <option th:each="item:${r'${'}application.CrmSysUserListCustMgr}" th:value="${r'${'}item.userId}" th:text="${r'${'}item.userName}" th:selected="${r'${'}item.userId==queryParams.custManagerId}"></option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="space-4"><input type="hidden" name="page" id="page"/></div>
                                </form>
                                <!--数据操作工具栏开始-->
                                <table width="100%" class="CSSearchTbl" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td  >
                                            <a class="btn btn-primary   "  id="btn-search" >
                                                开始搜索
                                                <i class="ace-icon fa fa-search"></i>
                                            </a>
                                            <a href="#">
                                                <button class="btn btn-success   "  >
                                                    添加文章
                                                    <i class="ace-icon fa fa-pencil "></i>
                                                </button>
                                            </a>
                                            <a class="btn btn-danger"  data-toggle="modal" data-target="#delWarning">
                                                批量删除
                                                <i class="ace-icon fa fa-trash-o"></i>
                                            </a>
                                        </td>



                                    </tr>

                                </table>
                                <!-- 分割线-->
                                <div class="space-4"> </div>

                                <!--数据表格开始-->
                                <div  id="table-container">

                                    <table id="sample-table-2" class="table table-striped table-bordered table-hover">
                                        <thead>
                                        <tr>
                                            <th class="center">
                                                <label class="position-relative">
                                                    <input type="checkbox" class="ace" />
                                                    <span class="lbl"></span>
                                                </label>
                                            </th>
                                            <#list attrs as attr>
                                            <th>${attr.comment}</th>
                                            </#list>

                                            <th>操作</th>
                                        </tr>
                                        </thead>

                                        <tbody>

                                        <tr th:each="item:${r'${'}${className}List}">
                                            <!--复选框-->
                                            <td class="center">
                                                <label class="position-relative">
                                                    <input type="checkbox" class="ace" th:value="${r'${'}item.${primaryProp}}" />
                                                    <span class="lbl"></span>
                                                </label>
                                            </td>
                                            <!--具体数据-->
                                            <#list attrs as attr>
                                                <#if  attr.type=="java.util.Date" >
                                                 <td   th:text="${r'${'}#dates.format(item.${attr.name},'yyyy-MM-dd')}"> </td>
                                                <#else>
                                                <td   th:text="${r'${'}item.${attr.name}}"> </td>
                                                </#if>
                                            </#list>
                                            <!--操作按钮-->
                                            <td>
                                                <div class="hidden-sm hidden-xs btn-group">
                                                    <a  th:href="${r'${'}'/${className}?${primaryProp}='+item.${primaryProp}}">
                                                        <button class="btn btn-xs btn-success" title="">
                                                            <i class="ace-icon fa fa-search-plus bigger-120"></i>
                                                            编辑
                                                        </button>
                                                    </a>
                                                    <a class="btn btn-xs btn-info">
                                                        <i class="ace-icon fa fa-pencil bigger-120"></i>
                                                        查看该客户订单
                                                    </a>

                                                    <a class="btn btn-xs btn-danger">
                                                        <i class="ace-icon fa fa-trash-o bigger-120"></i>
                                                        查看客户开发情况
                                                    </a>


                                                </div>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>

                                    <div class="modal-footer no-margin-top">
                                        <span class=" pull-left no-margin"  >共计</span>
                                        <span class=" pull-left no-margin" th:text="${r'${'}pageInfo.total}" style="color: #00B83F"></span>
                                        <span class=" pull-left no-margin"  >条记录</span>
                                        <span class=" pull-left no-margin"  >&nbsp;&nbsp;共计</span>
                                        <span class=" pull-left no-margin" th:text="${r'${'}pageInfo.pages}"></span>
                                        <span class=" pull-left no-margin"  >页</span>

                                        <span class=" pull-left no-margin"  >&nbsp;&nbsp;当前在第</span>
                                        <span class=" pull-left no-margin" th:text="${r'${'}pageInfo.pageNum}"></span>
                                        <span class=" pull-left no-margin"  >页</span>


                                        <span class=" pull-right no-margin"  >
                                        <ul class="pagination pull-left no-margin" >
                                            <li th:class="${r'${'}pageInfo.isFirstPage}?'disabled':''" ><a th:href="'javascript:gotopage(1)'" >首页</a></li>
                                            <li th:class="${r'${'}pageInfo.hasPreviousPage}?'':'disabled'" ><a th:href="'javascript:gotopage('+${r'${'}pageInfo.prePage}+')'">&laquo;</a></li>
                                            <li th:each="i:${r'${'}pageInfo.navigatepageNums}"   th:class="${r'${'}pageInfo.pageNum==i}?'active':''">
                                                <a   th:href="'javascript:gotopage('+${r'${'}i}+')'" th:text="${r'${'}i}" ></a>
                                            </li>
                                            <li  th:class="${r'${'}pageInfo.hasNextPage}?'':'disabled'" ><a th:href="'javascript:gotopage('+${r'${'}pageInfo.nextPage}+')'">&raquo;</a></li>
                                            <li th:class="${r'${'}pageInfo.isLastPage}?'disabled':''" ><a th:href="'javascript:gotopage('+${r'${'}pageInfo.pages}+')'">尾页</a></li>
                                        </ul>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 删除警告框-->
                        <!-- 模态框（Modal） -->
                        <div class="modal fade" id="delWarning" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                            &times;
                                        </button>
                                        <h4 class="modal-title" id="myModalLabel">
                                            确认删除
                                        </h4>
                                    </div>
                                    <div class="modal-body">
                                        您确认要删除吗？
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                                        </button>
                                        <button type="button" class="btn btn-primary">
                                            确认删除
                                        </button>
                                    </div>
                                </div><!-- /.modal-content -->
                            </div><!-- /.modal -->
                        </div>
                        <!-- PAGE CONTENT ENDS -->
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.page-content-area -->
        </div><!-- /.page-content -->
    </div><!-- /.main-content -->



    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div><!-- /.main-container -->


<!--[if !IE]> -->
<script type="text/javascript" th:inline="javascript">
    // <![CDATA[
    window.jQuery || document.write("<script src='/static/assets/js/jquery.min.js'>"+"<"+"/script>");
    // ]]>
</script>
<!-- <![endif]-->
<!--[if IE]>
<script type="text/javascript"  th:inline="javascript">
    window.jQuery || document.write("<script src='/static/assets/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript" th:inline="javascript">
    // <![CDATA[
    if('ontouchstart' in document.documentElement) document.write("<script src='/static/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
    // ]]>
</script>
<script src="/static/assets/js/bootstrap.min.js" th:inline="javascript"></script>
<script src="/static/laydate/laydate.js"></script>

<!--[if lte IE 8]>
<script src="/static/assets/js/excanvas.min.js" th:inline="javascript"></script>
<![endif]-->
<script src="/static/assets/js/jquery-ui.custom.min.js" th:inline="javascript"></script>
<script src="/static/assets/js/jquery.ui.touch-punch.min.js" th:inline="javascript"></script>.
<script src="/static/assets/js/ace-extra.min.js"></script>
<script src="/static/assets/js/ace-elements.min.js" th:inline="javascript"></script>
<script src="/static/assets/js/ace.min.js" th:inline="javascript"></script>
<script type="text/javascript">

    jQuery(function($) {

        //时间选择器
        laydate.render({
            elem: '#beginDate'
            ,type: 'datetime'
        });
        laydate.render({
            elem: '#endDate'
            ,type: 'datetime'
        });
        $( "#btn-search" ).click(function () {
            $("#form-${className}" ).submit();

        });
        function gotopage(pageNo) {
            $("#page").val(pageNo);
            $("#form-${className}" ).submit();

        }
        window.gotopage=gotopage;

    });
</script>
</body>
</html>