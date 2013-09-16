//
//  InterfaceFunctions.m
//  Estel
//
//  Created by Beleriand on 14/03/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "InterfaceFunctions.h"


@implementation InterfaceFunctions

/*
 * Creates a Round Rect Button that is inserted in the view
 */
+ (void)createNormalButton:	(NSObject *) sender
				   button: (UIButton *) bMyButton 
				   atPosX: (double) X 
				   atPosY: (double) Y 
				withSizeY: (double) HEIGHT 
				withSizeX: (double) WIDTH 
				withColor: (UIColor *) cMyColor 
				  withTag:(int) iTag 
				withTitle: (NSString *) sTitle
				   inView: (UIView *) Vista {
	bMyButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];											//The button is allocated
	[bMyButton setFrame:CGRectMake(X, Y, WIDTH, HEIGHT)];													//The type and size of the button is setted
	[bMyButton setTitle: sTitle forState:UIControlStateNormal];												//The button's title is setted 
	//[bMyButton setTitleShadowColor:cMyColor forState:UIControlStateNormal];								//The button's title shadow color is setted for its normal state
	//[bMyButton setBackgroundColor: cMyColor];																//The button's background color is setted
	[bMyButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];							//The button's title color is setted for its normal state
	[bMyButton setTag: iTag];																				//The button's tag is setted in order to select the proper the action
	[bMyButton addTarget: sender action:@selector(mainButtonAction:) forControlEvents:UIControlEventTouchUpInside]; //The action method is setted
	[Vista addSubview:bMyButton];																			//The button is added to the view
}

+ (void)setTextFieldProperties: (NSObject *) sender withPlaceHolder: (NSString *) sPlaceHolder {
	UITextField * tf = (UITextField *) sender;
	
	[tf setPlaceholder: sPlaceHolder];
	[tf setFont:[UIFont fontWithName:@"Verdana" size:12]];
	[tf setTextAlignment:UITextAlignmentCenter];
	[tf setAdjustsFontSizeToFitWidth:YES];
	[tf setBorderStyle:UITextBorderStyleNone];
	[tf setBorderStyle:UITextBorderStyleLine];
	[tf setBorderStyle:UITextBorderStyleBezel];
	[tf setBorderStyle:UITextBorderStyleRoundedRect];
	[tf setTextColor: [UIColor blueColor]];
	
	[tf becomeFirstResponder];	//This is set for the keyboard to disappear with resignFirstResponder when tap in background

	//tf = nil;
	
}

+ (void)setLabelProperties: (NSObject *) sender withCaption: (NSString *) sCaption {
	UILabel * lb = (UILabel *) sender;
	
	[lb setText: sCaption];
	[lb setFont:[UIFont fontWithName:@"Baskerville-Bold" size:20]];
	[lb setBackgroundColor:[UIColor clearColor]]; 
	[lb setTextAlignment:UITextAlignmentCenter];
	[lb setTextColor: [UIColor blackColor]];
	
	[lb setShadowColor: [UIColor grayColor]];
	[lb setShadowOffset: CGSizeMake(3, 3)];
	
}

/*
 * Creates an Image Button that is inserted in the view
 */
+ (void)createImageButton:	(NSObject *) sender
					button: (UIButton *) bMyButton 
					atPosX: (double) X 
					atPosY: (double) Y 
				 withSizeY: (double) HEIGHT 
				 withSizeX: (double) WIDTH 
				 withColor: (UIColor *) cMyColor 
				   withTag: (int) iTag 
				 withTitle: (NSString *) sTitle
					inView: (UIView *) Vista
				 withImage: (UIImage *) normalImage
		  withPressedImage: (UIImage *) pressedImage {
	
	bMyButton = [UIButton buttonWithType:UIButtonTypeCustom];												//The custom button is allocated
	[bMyButton setFrame:CGRectMake(X, Y, WIDTH, HEIGHT)];													//The type and size of the button is setted
	[bMyButton setTitle: sTitle forState:UIControlStateNormal];												//The button's title is setted 
	//[bMyButton setTitleShadowColor:cMyColor forState:UIControlStateNormal];								//The button's title shadow color is setted for its normal state
	[bMyButton setBackgroundColor: [UIColor clearColor]];													//The button's background color is setted
	[bMyButton setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];							//The button's title color is setted for its normal state
	[bMyButton setTag: iTag];																				//The button's tag is setted in order to select the proper the action
	[bMyButton addTarget: sender action:@selector(buttonClicked:) forControlEvents:UIControlEventTouchUpInside]; //The action method is setted
//	UIImage *newImage = [image stretchableImageWithLeftCapWidth:12.0 topCapHeight:0.0];
	[bMyButton setBackgroundImage:normalImage forState:UIControlStateNormal];	
//	UIImage *newPressedImage = [imagePressed stretchableImageWithLeftCapWidth:12.0 topCapHeight:0.0];
	[bMyButton setBackgroundImage:pressedImage forState:UIControlStateHighlighted];	
	[Vista addSubview:bMyButton];																			//The button is added to the view
}

@end
