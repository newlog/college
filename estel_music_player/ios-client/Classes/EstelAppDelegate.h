#import <UIKit/UIKit.h>
#import "Constants.h"
#import "Connection.h"

@class EstelViewController;

@interface EstelAppDelegate : NSObject <UIApplicationDelegate, UITabBarControllerDelegate> {
    UIWindow *window;
    EstelViewController *viewController;
	
	UITabBarController * controlTab;
	UINavigationController * vista1, * vista2;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet EstelViewController *viewController;

@property (nonatomic, retain) UITabBarController * controlTab;
@property (nonatomic, retain) UINavigationController * vista1;
@property (nonatomic, retain) UINavigationController * vista2;

@end

