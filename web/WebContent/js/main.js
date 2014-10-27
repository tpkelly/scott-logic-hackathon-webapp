$(function() {
	var self = this;
	
    var companies = ["Amazon", "Yahoo", "Tesco"];
    
    var source   = $("#template").html();
    var template = Handlebars.compile(source);
    var total = 0;
    
    function getData(company) {
    	$.getJSON("getResult", {company: company})
    		.done(function(data) {
    			$("#content").append(template(data));
    			
    			//add chart
    			var chart = new self.chartData();
    			chart.series = data.chartData;
    			$('#chart' + company).highcharts(chart);
    			
    			total += data.output.totalFunds;
    			$('footer h1').html("Total: " + formatMoney(total));
	    	}, this);
    };
    
    
    for (var i = 0; i < companies.length; i++) {
    	getData(companies[i]);
    };
    
    
    this.chartData = function() {
    	return {
    
            title: {
                text: 'Investments',
            },
            yAxis: [{
	            	title: {
	                    text: 'Close price'
	                },
                }, {
	                title: {
	                    text: 'Investments'
	                },
	                opposite: true
            }],
            tooltip: {
                enabled: false
            },
            plotOptions: {
            	line: {
            		marker: {
            			enabled: false
            		},
		            enableMouseTracking: false
            	}
            },
            chart: {
            	backgroundColor:'transparent'
            }
    	};
    };
});

function formatMoney (amount) {
	amount = amount + "";
    var j = (j = amount.length) > 3 ? j % 3 : 0;
    return "\xA3" + (j ? amount.substr(0, j) + ',' : "") + amount.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + ',');
}

Handlebars.registerHelper('formatMoney', formatMoney);

