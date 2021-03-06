#import <UIKit/UIKit.h>
#import "CacheManager.h"

@interface MainMenuViewController : UIViewController {

	CacheManager* cm;
}

- (IBAction) logoutAction: (id) sender;
- (IBAction) supportAction: (id) sender;
- (IBAction) processesAction: (id) sender;
- (IBAction) assetsAction: (id) sender;
- (IBAction) scenariosAction: (id) sender;
- (IBAction) incidentsAction: (id) sender;
- (IBAction) recoveryAction: (id) sender;
- (IBAction) notifyAction : (id) sender;

@end
