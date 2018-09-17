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

        <div class="page-content">
            <!-- /section:settings.box -->
            <div class="page-content-area">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <form id="form-${className}" class="form-horizontal" role="form"   >
                        <#list attrs as attr>
                            <div class="form-group" >
                                <label class="col-sm-2 control-label no-padding-right" for="${attr.name}"> ${attr.comment}：</label>
                                <div class="col-sm-10">
                                    <input type="text" id="${attr.name}" name="${attr.name}"
                                           placeholder="<#if attr.defaults??>${attr.defaults}<#else></#if>" class="col-xs-10 col-sm-5" th:value="${r'${'}${className}Dto.${attr.name}}"/>
                                </div>
                            </div>
                            <div class="space-4"></div>
                        </#list>
                            <div class="clearfix form-actions">
                                <div class="col-md-offset-3 col-md-9">
                                    <button id="btn-submit" class="btn btn-info" type="button">
                                        <i class="ace-icon fa fa-check bigger-110"></i>
                                        <span th:text="${r'${'}${className}Dto.==null?'添加':'修改'}"></span>
                                    </button>

                                    &nbsp; &nbsp; &nbsp;
                                    <button id="btn-backup" class="btn" type="reset">
                                        <i class="ace-icon fa fa-undo bigger-110"></i>
                                        返回
                                    </button>
                                </div>
                            </div>



                        </form>

                        <!-- PAGE CONTENT ENDS -->
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.page-content-area -->
        </div><!-- /.page-content -->
    </div><!-- /.main-content -->
    <!-- 添加修改信息结果警告框-->
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="postResWarning" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        添加结果
                    </h4>
                </div>
                <div class="modal-body">
                    <h4 class="modal-title"  id="postResContent" >
                        操作结果
                    </h4>
                </div>
                <div class="modal-footer">

                    <button type="button" class="btn btn-primary" data-dismiss="modal">
                        确认
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>


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
<!--[if lte IE 8]>
<script src="/static/assets/js/excanvas.min.js" th:inline="javascript"></script>
<![endif]-->
<script src="/static/assets/js/jquery-ui.custom.min.js" th:inline="javascript"></script>
<script src="/static/assets/js/jquery.ui.touch-punch.min.js" th:inline="javascript"></script>.
<script src="/static/assets/js/ace-extra.min.js"></script>
<script src="/static/assets/js/ace-elements.min.js" th:inline="javascript"></script>
<script src="/static/assets/js/ace.min.js" th:inline="javascript"></script>
<script type="text/javascript" th:inline="javascript">
    /*<![CDATA[*/
    jQuery(function($) {



        $( "#btn-backup" ).click(function () {
            window.history.back();  //返回上一页
        });

        var submitType=[[${r'${'}${className}Dto.${primaryProp}==null?'POST':'PUT'}]];
        $( "#btn-submit" ).click(function () {


            var d = {};
            var t = $("#form-${className}").serializeArray();
            $.each(t, function() {
                d[this.name] = this.value;
            });

            $.ajax({
                url :  "/${className}",
                type :submitType,
                data : JSON.stringify(d)  , //转JSON字符串
                contentType:'application/json;charset=UTF-8', //contentType很重要
                success: function (msg) {
                    $("#postResWarning").modal('show');
                    $("#postResContent").html(msg);
                },
                complete: function (data) {

                },
                error: function (XMLHttpRequest, textStatus, thrownError) {
                }
            });

        });



    });
    /*]]>*/
</script>

</body>
</html>