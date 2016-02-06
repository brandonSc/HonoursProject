module.exports = function(app) {
	
    var largeNumber = 5056530125034292182439436337056723800463181629800744005252906860109785352105082510253715567863347519484154579262768925987519610277940169682359736139258818047824720566904998970963407492662518872571713*7777777777;

    app.get('/long-operation', function(req, res) { 
	    //console.log(factor(largeNumber));
        factor(largeNumber);
        res.header('Content-Type', 'application/json');
        res.send('{"message":"done"}');
    });

	function factor(n) {
		if (isNaN(n) || !isFinite(n) || n%1!=0 || n==0) return ''+n;
		if (n<0) return '-'+factor(-n);
		var minFactor = leastFactor(n);
		if (n==minFactor) return ''+n;
		return minFactor+'*'+factor(n/minFactor);
	}

	// find the least factor in n by trial division
	function leastFactor(n) {
		if (isNaN(n) || !isFinite(n)) return NaN;  
		if (n==0) return 0;  
		if (n%1 || n*n<2) return 1;
		if (n%2==0) return 2;  
		if (n%3==0) return 3;  
		if (n%5==0) return 5;  
		var m = Math.sqrt(n);
		for (var i=7;i<=m;i+=30) {
			if (n%i==0)      return i;
			if (n%(i+4)==0)  return i+4;
			if (n%(i+6)==0)  return i+6;
			if (n%(i+10)==0) return i+10;
			if (n%(i+12)==0) return i+12;
			if (n%(i+16)==0) return i+16;
			if (n%(i+22)==0) return i+22;
			if (n%(i+24)==0) return i+24;
		}
		return n;
	}
}
