<html>
<head>
  <title></title>
</head>
<body>
  	<#assign text="${address}" />
	<#assign data=text?eval />

<p>CHICUN尺寸—订单收据</p>
<p>感谢您在 CHICUN尺寸 购物，我们已经收到了您的订单。</p>
<p>现货产品即将为您安排发货，预售产品会立即联系设计师为您安排生产。</p>
<p>您的订单编号为:${orderNos}</p>
<p>【请注意】CHICUN不会以任何订单异常为由，主动联系您让您在相关网页上提供财务信息进行退款或重新支付。如果您发现账户异常，请联系尺寸客服。</p>
<p>订单信息</p>
<hr/>
<table style="border-collapse:collapse;border:none;margin:20px 0px;text-align:center;width:100%;max-width:800px;">
  <tr>
    <th style="border:solid #000 1px;" width="30%">邮件</th>
    <th style="border:solid #000 1px;" width="40%">配送地址</th>
    <th style="border:solid #000 1px;" width="30%">支付方式</th>
  </tr>
  <tr>
    <td style="border:solid #000 1px;">${data.email}</td>
    <td style="border:solid #000 1px;">
	    ${data.recipients}<br/>
	    ${data.addressInfo}<br/>
    <#if data.country=="中国">
        ${data.province},${data.city},${data.region}<br/>
        ${data.postCode}<br/>
        中国<br/>
    <#else> 
        ${data.town!},${data.district!}
        ${data.postCode}<br/>
        ${data.country}<br/>
    </#if>
      ${data.mobileNo!}<br/>
    </td>
    <td style="border:solid #000 1px;">${payId!}</td>
  </tr>
</table>
<table style="border-collapse:collapse;border:none;margin:20px 0px;text-align:center;width:100%;max-width:800px;">
  <tr>
    <th style="border:solid #000 1px;" width="20%">所购商品</th>
    <th style="border:solid #000 1px;" width="40%"></th>
    <th style="border:solid #000 1px;" width="10%">状态</th>
    <th style="border:solid #000 1px;" width="10%">数量</th>
    <th style="border:solid #000 1px;" width="10%">单价</th>
    <th style="border:solid #000 1px;" width="10%">合计</th>
  </tr>
    <#list orderDetails as item>
        <tr>
            <td rowspan="3" style="border:solid #000 1px;width:20%;max-width:20%;padding:10px;"><img src='http://chicunbucket.oss-cn-shanghai.aliyuncs.com/chicun/${item.picture!}' style="width:100%;height:auto;"/></td>
            <td style="border:solid #000 1px;">${item.designerName}</td>
            <td rowspan="3" style="border:solid #000 1px;">
                <#if item.productState==10>
                          现货
                </#if>
                <#if item.productState==20>
                         预售
                </#if>
            </td>
            <td rowspan="3" style="border:solid #000 1px;">${item.ptBuyNum}</td>
            <td rowspan="3" style="border:solid #000 1px;">${inSign!}${item.price}</td>
            <td rowspan="3" style="border:solid #000 1px;">${inSign!}${item.ptsumMoney}</td>
        </tr>
        <tr>
          <td style="border:solid #000 1px;">${item.productName}</td>
        </tr>
        <tr>
          <td style="border:solid #000 1px;">${item.colorName!}&nbsp;&nbsp;${item.sizeName!}</td>
        </tr>
    </#list>
  <tr>
    <td colspan="6" style="text-align:right;border:solid #000 1px;">
        	总计:${outSign!}${payAbleMoney!}
    </td>
  </tr>
</table>
<p>
    注：部分预售产品由于生产工艺环节，制作流程需要时间，请您耐心等待！预售产品生产完成后，我们将立刻通知您，补齐尾款后会立即为您安排发货。
</p>
<p>
   需要帮助？发送电邮至： customercare@chicunglobal.com，每周7天，每天24小时全天候为您服务。
</p>
<p>CHICUN尺寸</p>
<p>客户服务部</p>
</body>
</html>