Sonatype.Events.addListener( 'repositoryMenuInit',
  function( menu, repoRecord ) {
      menu.add({
        text: 'Alert Something',
        handler: function( rec ) {
          alert('My First Plugin');
        },
        scope: this
      });
  }
);