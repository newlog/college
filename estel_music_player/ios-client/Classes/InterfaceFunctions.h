//
//  InterfaceFunctions.h
//  Estel
//
//  Created by Beleriand on 14/03/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#include "Main.h"

@interface InterfaceFunctions : NSObject {

}


+ (void) setTextFieldProperties: (NSObject *) sender withPlaceHolder: (NSString *) sPlaceHolder;
+ (void) setLabelProperties: (NSObject *) sender withCaption: (NSString *) sCaption;
+ (void) createNormalButton: (NSObject *) sender
				   button: (UIButton *) bMyButton 
				   atPosX: (double) X 
				   atPosY: (double) Y 
				withSizeY: (double) HEIGHT 
				withSizeX: (double) WIDTH 
				withColor: (UIColor *) cMyColor 
				  withTag:(int) iTag 
				withTitle: (NSString *) sTitle 
				   inView: (UIView *) Vista;
+ (void) createImageButton: (NSObject *) sender
					 button: (UIButton *) bMyButton 
					 atPosX: (double) X 
					 atPosY: (double) Y 
				  withSizeY: (double) HEIGHT 
				  withSizeX: (double) WIDTH 
				  withColor: (UIColor *) cMyColor 
					withTag:(int) iTag 
				  withTitle: (NSString *) sTitle 
					 inView: (UIView *) Vista
				  withImage:(UIImage *) normalImage
  		   withPressedImage: (UIImage *) pressedImage;

@end
