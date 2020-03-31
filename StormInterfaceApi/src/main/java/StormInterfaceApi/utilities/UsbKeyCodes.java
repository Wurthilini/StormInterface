package StormInterfaceApi.utilities;

import java.util.HashMap;
import java.util.Map;

public enum UsbKeyCodes {

	 usbUsageModifier((byte) 0x00),
	 usbUsageReserved((byte) 0x00),
	 usbUsageErrorRollOver((byte) 0x01),
	 usbUsagePOSTFail((byte) 0x02),
	 usbUsageErrorUndefined((byte) 0x03),
	 usbUsageA((byte) 0x04),
	 usbUsageB((byte) 0x05),
	 usbUsageC((byte) 0x06),
	 usbUsageD((byte)0x07),
	 usbUsageE((byte)0x08),
	 usbUsageF((byte)0x09),
	 usbUsageG((byte)0x0A),
	 usbUsageH((byte)0x0B),
	 usbUsageI((byte)0x0C),
	 usbUsageJ((byte) 0x0D),
	 usbUsageK((byte) 0x0E),
	 usbUsageL((byte) 0x0F),
	 usbUsageM((byte) 0x10),
	 usbUsageN((byte) 0x11),
	 usbUsageO((byte) 0x12),
	 usbUsageP((byte) 0x13),
	 usbUsageQ((byte) 0x14),
	 usbUsageR((byte) 0x15),
	 usbUsageS((byte) 0x16),
	 usbUsageT((byte) 0x17),
	 usbUsageU((byte) 0x18),
	 usbUsageV((byte) 0x19),
	 usbUsageW((byte) 0x1A),
	 usbUsageX((byte) 0x1B),
	 usbUsageY((byte) 0x1C),
	 usbUsageZ((byte) 0x1D),
	 usbUsage1((byte) 0x1E),
	 usbUsage2((byte) 0x1F),
	 usbUsage3((byte) 0x20),
	 usbUsage4((byte) 0x21),
	 usbUsage5((byte) 0x22),
	 usbUsage6((byte) 0x23),
	 usbUsage7((byte) 0x24),
	 usbUsage8((byte) 0x25),
	 usbUsage9((byte) 0x26),
	 usbUsage0((byte) 0x27),
	 usbUsageEnter((byte) 0x28),
	 usbUsageEscape((byte) 0x29),
	 usbUsageBackspace((byte) 0x2A),
	 usbUsageTab((byte) 0x2B),
	 usbUsageSpacebar((byte) 0x2C),
	 usbUsageMinus((byte) 0x2D),
	 usbUsageEqual((byte) 0x2E),
	 usbUsageLeftBracket((byte) 0x2F),
	 usbUsageRightBracket((byte) 0x30),
	 usbUsageBackslash((byte) 0x31),
	 usbUsageVerticalBar((byte) 0x32),
	 usbUsageSemicolon((byte) 0x33),
	 usbUsageApostrophe((byte) 0x34),
	 usbUsageTilde((byte) 0x35),
	 usbUsageComma((byte) 0x36),
	 usbUsagePeriod((byte) 0x37),
	 usbUsageSlash((byte) 0x38),
	 usbUsageCapsLock((byte) 0x39),
	 usbUsageF1((byte) 0x3A),
	 usbUsageF2((byte) 0x3B),
	 usbUsageF3((byte) 0x3C),
	 usbUsageF4((byte) 0x3D),
	 usbUsageF5((byte) 0x3E),
	 usbUsageF6((byte) 0x3F),
	 usbUsageF7((byte) 0x40),
	 usbUsageF8((byte) 0x41),
	 usbUsageF9((byte) 0x42),
	 usbUsageF10((byte) 0x43),
	 usbUsageF11((byte) 0x44),
	 usbUsageF12((byte) 0x45),
	 usbUsagePrintScreen((byte) 0x46),
	 usbUsageScrollLock((byte) 0x47),
	 usbUsagePause((byte) 0x48),
	 usbUsageInsert((byte) 0x49),
	 usbUsageHome((byte) 0x4A),
	 usbUsagePageUp((byte) 0x4B),
	 usbUsageDeleteForward((byte) 0x4C),
	 usbUsageEnd((byte) 0x4D),
	 usbUsagePageDown((byte) 0x4E),
	 usbUsageRightArrow((byte) 0x4F),
	 usbUsageLeftArrow((byte) 0x50),
	 usbUsageDownArrow((byte) 0x51),
	 usbUsageUpArrow((byte) 0x52),
	 usbUsageKeypadNumlock((byte) 0x53),
	 usbUsageKeypadSlash((byte) 0x54),
	 usbUsageKeypadAsterisk((byte) 0x55),
	 usbUsageKeypadMinus((byte) 0x56),
	 usbUsageKeypadPlus((byte) 0x57),
	 usbUsageKeypadEnter((byte) 0x58),
	 usbUsageKeypad1((byte) 0x59),
	 usbUsageKeypad2((byte) 0x5A),
	 usbUsageKeypad3((byte) 0x5B),
	 usbUsageKeypad4((byte) 0x5C),
	 usbUsageKeypad5((byte) 0x5D),
	 usbUsageKeypad6((byte) 0x5E),
	 usbUsageKeypad7((byte) 0x5F),
	 usbUsageKeypad8((byte) 0x60),
	 usbUsageKeypad9((byte) 0x61),
	 usbUsageKeypad0((byte) 0x62),
	 usbUsageKeypadPeriod((byte) 0x63),
	 usbUsageNonUsBackslash((byte) 0x64),
	 usbUsageWindowsKey((byte) 0x65),
	 usbUsagePower((byte) 0x66),
	 usbUsageKeypadEqual((byte) 0x67),
	 usbUsageF13((byte) 0x68),
	 usbUsageF14((byte) 0x69),
	 usbUsageF15((byte) 0x6A),
	 usbUsageF16((byte) 0x6B),
	 usbUsageF17((byte) 0x6C),
	 usbUsageF18((byte) 0x6D),
	 usbUsageF19((byte) 0x6E),
	 usbUsageF20((byte) 0x6F),
	 usbUsageF21((byte) 0x70),
	 usbUsageF22((byte) 0x71),
	 usbUsageF23((byte) 0x72),
	 usbUsageF24((byte) 0x73),
	 usbUsageExecute((byte) 0x74),
	 usbUsageHelp((byte) 0x75),
	 usbUsageMenu((byte) 0x76),
	 usbUsageSelect((byte) 0x77),
	 usbUsageStop((byte) 0x78),
	 usbUsageAgain((byte) 0x79),
	 usbUsageUndo((byte) 0x7A),
	 usbUsageCut((byte) 0x7B),
	 usbUsageCopy((byte) 0x7C),
	 usbUsagePaste((byte) 0x7D),
	 usbUsageFind((byte) 0x7E),
	 usbUsageMute((byte) 0x7F),
	 usbUsageVolumeUp((byte) 0x80),
	 usbUsageVolumneDown((byte) 0x81),
	 usbUsageLockingCapsLock((byte) 0x82),
	 usbUsageLeftShift((byte) 0xE1),
	 usbUsageLeftAlt((byte) 0xE2),
	 usbUsageLeftGUI((byte) 0xE3),
	 usbUsageRightControl((byte) 0xE4),
	 usbUsageRightShift((byte) 0xE5),
	 usbUsageRightAlt((byte) 0xE6),
	 usbUsageRightGUI((byte) 0xE7);
	
	 private final byte value;
	 UsbKeyCodes(byte value)
	 {
		 this.value = value;
	 }
	public byte value()
	{
		return this.value;
	}
	private static Map<Byte, UsbKeyCodes> map = new HashMap<>();
	
	static
	{
		for(UsbKeyCodes keyCodeID : UsbKeyCodes.values())
			map.put(keyCodeID.value(), keyCodeID);
	}
}