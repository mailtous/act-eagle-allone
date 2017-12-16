<nav-framework>
  <div class="heading pointer" onclick="{toggleDisplay}">
    <i class="fa fa-institution"></i>&nbsp;Frameworks
    <i class="fa fa-angle-right" if="{collapsed}"></i>
    <i class="fa fa-angle-up" if="{!collapsed}"></i>
  </div>
  <ul class="language-list menu nav-framework" if="{!collapsed}">
    <li each="{frameworkList, language in frameworks}" class="{language} {current: currentLanguage === language}" >
      <div class="language" onclick="{toggleLanguage}">
        <i class="fa fa-folder" if="{currentLanguage !== language}"></i>
        <i class="fa fa-folder-open" if="{currentLanguage === language}"></i>
        {language}
        <span class="float-right badge hover-visible">{frameworkList.length}</span>
      </div>
      <ul class="framework-list menu" if="{currentLanguage === language}">
        <li each="{framework in frameworkList}" onclick="{viewFramework}" class="{current: currentFramework === framework}">
          <i class="fa fa-file-text"></i>
          {framework}
        </li>
      </ul>
    </li>
  </ul>
  <script>
    var self = this
    self.frameworks = {}
    self.languageLookup = {}
    self.currentLanguage = false
    self.currentFramework = false
    self.collapsed = true

    var r = route.create();
    r("framework/*", function(framework) {
      if (!self.languageLookup[framework]) {
        setTimeout(function() {
          self._viewFramework(framework)
        }, 200);
      } else {
        self._viewFramework(framework)
      }
    });

    self.on('mount', function() {
      self.fetchFrameworks()
    });
    fetchFrameworks() {
      $.getJSON('/sys/home/my/top/menu.json?userId=1', function(data) {
        $.each(data, function(lang, frameworks) {
          for (var i = 0, j = frameworks.length; i < j; ++i) {
            self.languageLookup[frameworks[i]] = lang
          }
        });
        self.frameworks = data
        self.update()
      })
    };
    toggleDisplay() {
      self.collapsed = !self.collapsed
    };
    toggleLanguage(e) {
      if (e.item.language !== self.currentLanguage) {
        self.currentLanguage = e.item.language
      } else {
        self.currentLanguage = false
      }
    };
    viewFramework(e) {
      route('framework/' + e.item.framework)
    };
    _viewFramework(framework){
      self.currentFramework = framework
      self.currentLanguage = self.languageLookup[framework]
      riot.store.trigger('open', {view: 'framework', framework: self.currentFramework, language: self.currentLanguage});
    };
  </script>
</nav-framework>