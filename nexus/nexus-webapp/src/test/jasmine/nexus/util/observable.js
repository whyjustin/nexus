define(['nexus/util/observable'], function(underTest) {
  describe('Nexus.util.Observable', function() {
    it('should be defined', function() {
      expect(underTest).toBeTruthy();
    });

    var handler = {
      expect : function(id) {
        var arg = "notcalled";
        var real = function(a) {
          arg = a;
        };
        real.verify = function() {
          return id === arg;
        };
        return real;
      }
    };

    it('should be able to add listeners', function() {
      underTest.addListener('test', );
    });
  });
});