#import <UIKit/UIKit.h>
#import "InterfaceFunctions.h"
#import "Constants.h"


@interface EstelViewController : UIViewController {
	UIButton * bConnect;
	UIButton * bDisconnect;
	UIButton * bConfiguration;
	UIButton * bExit;
}

@property (nonatomic, retain) UIButton * bConnect, * bDisconnect, * bConfiguration, * bExit;


@end

