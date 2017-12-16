/*
 * thymeleaf-fragment.js 0.2.0
 * 
 * See: https://github.com/BlackPepperSoftware/thymeleaf-fragment.js
 */
function ThymeleafFragment() {

	// Prefix that gets prepended to view names when building a URL
	// 项目前端页面的基本路径
	var templatePrefix = '../act-eagle/act-eagel-sys/thymeleaf';

	// Suffix that gets appended to view names when building a URL
	var templateSuffix = '';

	this.processAttributes = function() {
		// Hold off scripts waiting for the document to be ready
		$.holdReady(true);
		
		var promises = [];
		addPromises(promises);
		
		// Release scripts once all fragments have been processed
		$.when.apply(null, promises).done(function() {
			$.holdReady(false);
		});
	};

	var addPromises = function(promises, element) {
		$('[th\\:include]', element).each(function() {
			var fragmentSpec = $(this).attr('th:include');
			var fragmentUri = resolveFragmentUri(fragmentSpec);
			$(this).removeAttr('th:include');
			promises.push(createLoadPromise(promises, this, fragmentUri, false, true));
		});
		
		$('[th\\:insert]', element).each(function() {
			var fragmentSpec = $(this).attr('th:insert');
			var fragmentUri = resolveFragmentUri(fragmentSpec);
			$(this).removeAttr('th:insert');
			promises.push(createLoadPromise(promises, this, fragmentUri, false, false));
		});
		
		$('[th\\:replace]', element).each(function() {
			var fragmentSpec = $(this).attr('th:replace');
			var fragmentUri = resolveFragmentUri(fragmentSpec);
			promises.push(createLoadPromise(promises, this, fragmentUri, true, false));
		});
	};
	
	var resolveFragmentUri = function(fragmentSpec) {
		var tokens = fragmentSpec.trim().split(/\s*::\s*/);
		var templateName = tokens[0];
		var fragmentExpression = tokens[1];
		
		var resourceName = resolveTemplate(templateName);
		var fragmentSelector = parseFragmentExpression(fragmentExpression);

		if (fragmentSelector === undefined) {
			return resourceName;
		}
		
		return resourceName + ' ' + fragmentSelector;
	};
	
	var parseFragmentExpression = function(fragmentExpression) {
		if (fragmentExpression === undefined) {
			return undefined;
		}

		// explicit DOM selector
		var domSelectorRegex = /\[(.*)]/;
		if (fragmentExpression.match(domSelectorRegex)) {
			return fragmentExpression.match(domSelectorRegex)[1];
		}
		
		// fragment name or implicit DOM selector
		var fragmentNameRegex = /([^()]*)/;
		var fragmentNameOrDomSelector = fragmentExpression.match(fragmentNameRegex)[1];
		return '[th\\:fragment^="' + fragmentNameOrDomSelector + '"], ' + fragmentNameOrDomSelector;
	};

	var resolveTemplate = function(templateName) {
		var link = document.createElement('a');
		link.href = templatePrefix + templateName + templateSuffix;
		return link.href;
	};

	var createLoadPromise = function(promises, element, url, replaceHost, insertOnlyContents) {
		return $.Deferred(function(deferred) {
			$(element).load(url, function() {
				var fragment = $(element).children().first().get();
				
				if (insertOnlyContents) {
					$(fragment).contents().first().unwrap();
				}
				else {
					$(fragment).removeAttr('th:fragment');
				}
				
				addPromises(promises, element);
				
				if (replaceHost) {
					$(fragment).unwrap();
				}
				
				deferred.resolve();
			});
		}).promise();
	};
}

new ThymeleafFragment().processAttributes();
