//
//  Main.h
//  Estel
//
//  Created by Beleriand on 15/03/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Constants.h"
#import "Configuration.h"
#import "Menu.h"

//@class Configuration;
@class Menu;

@interface Main : UIViewController {
	Menu * menuView;
}

@property (nonatomic, retain) Menu * menuView;


@end
