<!DOCTYPE html>
<%@page import="utilitaire.SessionUtil"%>
<%@page import="com.mapping.Utilisateur"%>
<% 
	String cible=SessionUtil.getValForAttr(request,"cible");
	String currmenu=SessionUtil.getValForAttr(request,"currmenu");
	String id=SessionUtil.getValForAttr(request,"id");
	try{
      	SessionUtil.isExisteSession(request);
      }
      catch(Exception ex)
      {
    	  %><script>document.location.replace("login.jsp?error=<%=ex.getMessage()%>&cible=<%=cible%>&currmenu=<%=currmenu%>&id=<%=id%>");</script><%
      		return ;
      }
try{
  	SessionUtil.testAcces(request);
  }
  catch(Exception ex)
  {
	  %><script language="JavaScript">
	  alert("Vous n'avez pas acces a cette page!");
         history.back();</script><%
  		return ;
  }
%>
<%try{%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Dashboard">
    <meta name="keyword" content="Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">

    <title>Resto admin</title>

    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <!--external css-->
    <link href="assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="assets/css/zabuto_calendar.css">
    <link rel="stylesheet" type="text/css" href="assets/js/gritter/css/jquery.gritter.css" />
    <link rel="stylesheet" type="text/css" href="assets/lineicons/style.css">    
    
    <!-- Custom styles for this template -->
    <link href="assets/css/style.css" rel="stylesheet">
    <link href="assets/css/style-responsive.css" rel="stylesheet">

    <script src="assets/js/chart-master/Chart.js"></script>
    
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

  <section id="container" >
      <!-- **********************************************************************************************************************************************************
      TOP BAR CONTENT & NOTIFICATIONS
      *********************************************************************************************************************************************************** -->
      <!--header start-->
      <header class="header black-bg">
              <div class="sidebar-toggle-box">
                  <div class="fa fa-bars tooltips" data-placement="right" data-original-title="Toggle Navigation"></div>
              </div>
            <!--logo start-->
            <a href="main.jsp?cible=stat" class="logo"><b>RESTO ADMIN</b></a>
            <!--logo end-->
            <div class="nav notify-row" id="top_menu">
                <!--  notification start -->
                <!-- <ul class="nav top-menu">
                    <!-- settings start -->
                    <!-- <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="index.html#">
                            <i class="fa fa-tasks"></i>
                            <span class="badge bg-theme">4</span>
                        </a>
                        <ul class="dropdown-menu extended tasks-bar">
                            <div class="notify-arrow notify-arrow-green"></div>
                            <li>
                                <p class="green">You have 4 pending tasks</p>
                            </li>
                            <li>
                                <a href="index.html#">
                                    <div class="task-info">
                                        <div class="desc">DashGum Admin Panel</div>
                                        <div class="percent">40%</div>
                                    </div>
                                    <div class="progress progress-striped">
                                        <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
                                            <span class="sr-only">40% Complete (success)</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="index.html#">
                                    <div class="task-info">
                                        <div class="desc">Database Update</div>
                                        <div class="percent">60%</div>
                                    </div>
                                    <div class="progress progress-striped">
                                        <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
                                            <span class="sr-only">60% Complete (warning)</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="index.html#">
                                    <div class="task-info">
                                        <div class="desc">Product Development</div>
                                        <div class="percent">80%</div>
                                    </div>
                                    <div class="progress progress-striped">
                                        <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 80%">
                                            <span class="sr-only">80% Complete</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="index.html#">
                                    <div class="task-info">
                                        <div class="desc">Payments Sent</div>
                                        <div class="percent">70%</div>
                                    </div>
                                    <div class="progress progress-striped">
                                        <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style="width: 70%">
                                            <span class="sr-only">70% Complete (Important)</span>
                                        </div>
                                    </div>
                                </a>
                            </li>
                            <li class="external">
                                <a href="#">See All Tasks</a>
                            </li>
                        </ul>
                    </li>
                    <!-- settings end -->
                    <!-- inbox dropdown start-->
                   <!--  <li id="header_inbox_bar" class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="index.html#">
                            <i class="fa fa-envelope-o"></i>
                            <span class="badge bg-theme">5</span>
                        </a>
                        <ul class="dropdown-menu extended inbox">
                            <div class="notify-arrow notify-arrow-green"></div>
                            <li>
                                <p class="green">You have 5 new messages</p>
                            </li>
                            <li>
                                <a href="index.html#">
                                    <span class="photo"><img alt="avatar" src="assets/img/ui-zac.jpg"></span>
                                    <span class="subject">
                                    <span class="from">Zac Snider</span>
                                    <span class="time">Just now</span>
                                    </span>
                                    <span class="message">
                                        Hi mate, how is everything?
                                    </span>
                                </a>
                            </li>
                            <li>
                                <a href="index.html#">
                                    <span class="photo"><img alt="avatar" src="assets/img/ui-divya.jpg"></span>
                                    <span class="subject">
                                    <span class="from">Divya Manian</span>
                                    <span class="time">40 mins.</span>
                                    </span>
                                    <span class="message">
                                     Hi, I need your help with this.
                                    </span>
                                </a>
                            </li>
                            <li>
                                <a href="index.html#">
                                    <span class="photo"><img alt="avatar" src="assets/img/ui-danro.jpg"></span>
                                    <span class="subject">
                                    <span class="from">Dan Rogers</span>
                                    <span class="time">2 hrs.</span>
                                    </span>
                                    <span class="message">
                                        Love your new Dashboard.
                                    </span>
                                </a>
                            </li>
                            <li>
                                <a href="index.html#">
                                    <span class="photo"><img alt="avatar" src="assets/img/ui-sherman.jpg"></span>
                                    <span class="subject">
                                    <span class="from">Dj Sherman</span>
                                    <span class="time">4 hrs.</span>
                                    </span>
                                    <span class="message">
                                        Please, answer asap.
                                    </span>
                                </a>
                            </li>
                            <li>
                                <a href="index.html#">See all messages</a>
                            </li>
                        </ul>
                    </li>
                    <!-- inbox dropdown end -->
                <!-- </ul>
                <!--  notification end -->
            </div>
            <div class="top-menu">
            	<ul class="nav pull-right top-menu">
                    <li><a class="logout" href="login.jsp">Deconnexion</a></li>
            	</ul>
            </div>
        </header>
      <!--header end-->
      
      <!-- **********************************************************************************************************************************************************
      MAIN SIDEBAR MENU
      *********************************************************************************************************************************************************** -->
      <!--sidebar start-->
      <aside>
          <div id="sidebar"  class="nav-collapse ">
              <!-- sidebar menu start-->
              <ul class="sidebar-menu" id="nav-accordion">
              
              	  <!-- <p class="centered"><a href="profile.html"><img src="assets/img/icon2.png" class="img-circle" width="60"></a></p>-->
              	  <h5 class="centered"><%=((Utilisateur)request.getSession().getAttribute("utilisateur")).getNom()%></h5>
                  <li class="mt">
                      <a class="active" id="menu-ecceuil" href="main.jsp?cible=stat&currmenu=menu-ecceuil" >
                          <i class="fa fa-dashboard"></i>
                          <span>Acceuil</span>
                      </a>
                  </li>

                  <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class="fa fa-cutlery"></i>
                          <span>Commande</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="main.jsp?cible=commande/commande-ajout&currmenu=menu-commmande-ajout" id="menu-commmande-ajout">Ajout commande</a></li>
                          <li><a  href="buttons.html">Liste des commandes</a></li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class="fa fa-phone-square"></i>
                          <span>Reservation</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="general.html">Liste des reservations</a></li>
                          <li><a  href="buttons.html">Ajout reservation</a></li>
                      </ul>
                  </li>

                  <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class="fa fa-table"></i>
                          <span>Table</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="main.jsp?cible=configuration/table-gestion&currmenu=menu-liste-table" id="menu-liste-table">Liste des tables</a></li>
                          <li><a  href="main.jsp?cible=configuration/table-ajout&currmenu=menu-ajout-table" id="menu-ajout-table">Ajout table</a></li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class="fa fa-cog"></i>
                          <span>Configuration stock</span>
                      </a>
                      <ul class="sub">
                      	 <li class="sub-menu">
			                      <a href="javascript:;" >
			                         <!--  <i class="fa fa-users"></i>-->
			                          <span>Article</span>
			                      </a>
			                      <ul class="sub"><li><a  href="main.jsp?cible=configuration/article-liste&currmenu=menu-article-liste" id="menu-article-liste">Liste des articles</a></li>
			                      <li><a  href="main.jsp?cible=configuration/article-ajout&currmenu=menu-article-ajout" id="menu-article-ajout">Ajout article</a></li>
			                      </ul>
	                      </li>
                          <li class="sub-menu">
			                      <a href="javascript:;" >
			                         <!--  <i class="fa fa-users"></i>-->
			                          <span>Unite d'article</span>
			                      </a>
			                      <ul class="sub"><li><a  href="main.jsp?cible=configuration/unite-liste&currmenu=menu-unite-liste" id="menu-unite-liste">Liste des unites</a></li>
			                      <li><a  href="main.jsp?cible=configuration/unite-ajout&currmenu=menu-unite-ajout" id="menu-unite-ajout">Ajout unite</a></li>
			                      </ul>
	                      </li>
	                      <li class="sub-menu">
			                      <a href="javascript:;" >
			                         <!--  <i class="fa fa-users"></i>-->
			                          <span>Categorie d'article</span>
			                      </a>
			                      <ul class="sub"><li><a  href="main.jsp?cible=configuration/categorie-liste&currmenu=menu-categorie-liste" id="menu-categorie-liste">Liste des categories</a></li>
			                      <li><a  href="main.jsp?cible=configuration/categorie-ajout&currmenu=menu-categorie-ajout" id="menu-categorie-ajout">Categorie ajout</a></li>
			                      </ul>
	                      </li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class="fa fa-cog"></i>
                          <span>Configuration Generale</span>
                      </a>
                      <ul class="sub">
	                      <li class="sub-menu">
			                      <a href="javascript:;" >
			                            <i class="fa fa-users"></i>
			                          <span>Utilisateur</span>
			                      </a>
			                      <ul class="sub"><li><a class="active"  href="main.jsp?cible=configuration/liste-utilisateur&currmenu=menu-utilisateur-liste" id="menu-utilisateur-liste">Liste des utilisateurs</a>
			                      <li><a  href="main.jsp?cible=configuration/utilisateur-ajout&currmenu=menu-utilisateur-ajout" id="menu-utilisateur-ajout">Ajout utilisateur</a>
			                      </li>
			                      </ul>
	                      </li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class="fa fa-cog"></i>
                          <span>Configuration Commande</span>
                      </a>
                      <ul class="sub">
	                      <li class="sub-menu">
			                      <a href="javascript:;" >
			                          <i class="fa fa-liste"></i>
			                          <span>Menu</span>
			                      </a>
			                      <ul class="sub"><li><a  href="main.jsp?cible=configuration/menu-liste&currmenu=menu-menu-liste" id="menu-menu-liste">Liste des menus</a>
			                      <li><a  href="main.jsp?cible=configuration/menu-ajout&currmenu=menu-menu-ajout" id="menu-menu-ajout">Ajout menu</a>
			                      </li>
			                      </ul>
	                      </li>
	                      <li class="sub-menu">
			                      <a href="javascript:;" >
			                          <i class="fa fa-object-group"></i>
			                          <span>Famille de menu</span>
			                      </a>
			                      <ul class="sub"><li><a  href="main.jsp?cible=configuration/famille-liste&currmenu=menu-famille-liste" id="menu-famille-liste">Liste des familles</a>
			                      <li><a  href="main.jsp?cible=configuration/famille-ajout&currmenu=menu-famille-ajout" id="menu-famille-ajout">Ajout famille</a>
			                      </li>
			                      </ul>
	                      </li>
                      </ul>
                  </li>
                  <!-- <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class="fa fa-tasks"></i>
                          <span>Forms</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="form_component.html">Form Components</a></li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class="fa fa-th"></i>
                          <span>Data Tables</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="basic_table.html">Basic Table</a></li>
                          <li><a  href="responsive_table.html">Responsive Table</a></li>
                      </ul>
                  </li>
                  <li class="sub-menu">
                      <a href="javascript:;" >
                          <i class=" fa fa-bar-chart-o"></i>
                          <span>Charts</span>
                      </a>
                      <ul class="sub">
                          <li><a  href="morris.html">Morris</a></li>
                          <li><a  href="chartjs.html">Chartjs</a></li>
                      </ul>
                  </li>-->

              </ul>
              <!-- sidebar menu end-->
          </div>
      </aside>
      <!--sidebar end-->
      
      <!-- **********************************************************************************************************************************************************
      MAIN CONTENT
      *********************************************************************************************************************************************************** -->
      <!--main content start-->
      <section id="main-content">
          <section class="wrapper">
          <%
          		cible="pages/"+cible+".jsp";
          		try{
          		%>
          		<jsp:include page='<%=cible%>'/>
                <% 
                }
          		catch (Exception e) {
          			%>
                    <script language="JavaScript"> alert('<%=e.getMessage().toUpperCase() %>');
                        history.back();</script>
                    <%
                }
          %>
          </section>
      </section>

      <!--main content end-->
      <!--footer start-->
      <footer class="site-footer">
          <div class="text-center">
              2016 - Resto Tamatave
              <a href="#" class="go-top">
                  <i class="fa fa-angle-up"></i>
              </a>
          </div>
      </footer>
      <!--footer end-->
  </section>

    <!-- js placed at the end of the document so the pages load faster -->
    <script src="assets/js/jquery.js"></script>
    <script src="assets/js/jquery-1.8.3.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script class="include" type="text/javascript" src="assets/js/jquery.dcjqaccordion.2.7.js"></script>
    <script src="assets/js/jquery.scrollTo.min.js"></script>
    <script src="assets/js/jquery.nicescroll.js" type="text/javascript"></script>
    <script src="assets/js/jquery.sparkline.js"></script>


    <!--common script for all pages-->
    <script src="assets/js/common-scripts.js"></script>
    
    <script type="text/javascript" src="assets/js/gritter/js/jquery.gritter.js"></script>
    <script type="text/javascript" src="assets/js/gritter-conf.js"></script>

    <!--script for this page-->
    <script src="assets/js/sparkline-chart.js"></script>    
	<script src="assets/js/zabuto_calendar.js"></script>	
	
	<script type="text/javascript">
       /* $(document).ready(function () {
        var unique_id = $.gritter.add({
            // (string | mandatory) the heading of the notification
            title: 'Notification',
            // (string | mandatory) the text inside the notification
            text: 'Hover me to enable the Close Button. You can hide the left sidebar clicking on the button next to the logo. Free version for <a href="http://blacktie.co" target="_blank" style="color:#ffd777">BlackTie.co</a>.',
            // (string | optional) the image to display on the left
            image: 'assets/img/ui-sam.jpg',
            // (bool | optional) if you want it to fade out on its own or just sit there
            sticky: true,
            // (int | optional) the time you want it to be alive for before fading out
            time: '',
            // (string | optional) the class name you want to apply to that specific message
            class_name: 'my-sticky-class'
        });

        return false;
        });*/
	</script>
	
	<script type="application/javascript">
        $(document).ready(function () {
            $("#date-popover").popover({html: true, trigger: "manual"});
            $("#date-popover").hide();
            $("#date-popover").click(function (e) {
                $(this).hide();
            });
        
            $("#my-calendar").zabuto_calendar({
                action: function () {
                    return myDateFunction(this.id, false);
                },
                action_nav: function () {
                    return myNavFunction(this.id);
                },
                ajax: {
                    url: "show_data.php?action=1",
                    modal: true
                },
                legend: [
                    {type: "text", label: "Special event", badge: "00"},
                    {type: "block", label: "Regular event", }
                ]
            });
        });
        
        
        function myNavFunction(id) {
            $("#date-popover").hide();
            var nav = $("#" + id).data("navigation");
            var to = $("#" + id).data("to");
            console.log('nav ' + nav + ' to: ' + to.month + '/' + to.year);
        }
        var defaults = {
    			classParent	 : 'dcjq-parent',
    			classActive	 : 'active',
    			classArrow	 : 'dcjq-icon',
    			classCount	 : 'dcjq-count',
    			classExpand	 : 'dcjq-current-parent',
    			eventType	 : 'click',
    			hoverDelay	 : 300,
    			menuClose     : true,
    			autoClose    : true,
    			autoExpand	 : false,
    			speed        : 'slow',
    			saveState	 : true,
    			disableLink	 : true,
    			showCount : false,
//    			cookie	: 'dcjq-accordion'
    		};
        var obj = this;
        function autoCloseAccordion($parentsLi, $parentsUl){
			$('ul',obj).not($parentsUl).slideUp(defaults.speed);
			// Reset active links
			$('a',obj).removeClass(defaults.classActive);
			$('> a',$parentsLi).addClass(defaults.classActive);
			$('> ul',$parentsLi).slideToggle(defaults.speed);
		}
        $(document).ready(function () {
        	$(".active").removeClass("active");
        	<%if(currmenu!=null && currmenu.compareToIgnoreCase("null")!=0){%>
        	active($("#<%=currmenu%>"));
        	<%}%>
        });
        function active(dom)
        {
        	$activeLi = dom.parent('li');
			$parentsLi = $activeLi.parents('li');
			$parentsUl = $activeLi.parents('ul');

			// Prevent browsing to link if has child links
			if(defaults.disableLink == true){
				if(dom.siblings('ul').length >0){
					e.preventDefault();
				}
			}

			// Auto close sibling menus
			if(defaults.autoClose == true){
				autoCloseAccordion($parentsLi, $parentsUl);
			}

			if ($('> ul',$activeLi).is(':visible')){
				$('ul',$activeLi).slideUp(defaults.speed);
				$('a',$activeLi).removeClass(defaults.classActive);
			} else {
				$(dom).siblings('ul').slideToggle(defaults.speed);
				$('> a',$activeLi).addClass(defaults.classActive);
			}
        	
        }
        
    </script>
  

  </body>
</html>

<%}
catch(Exception ex){
	
}
%>
