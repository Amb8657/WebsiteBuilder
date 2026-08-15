from pathlib import Path
p=Path('app/src/main/java/com/amb8657/websitebuilder/WebsiteBuilderV4Activity.java')
s=p.read_text()
s=s.replace('final int BG=CanvaDesignSystem.BG,PANEL=CanvaDesignSystem.PANEL,PANEL2=CanvaDesignSystem.PANEL_2,ACCENT=CanvaDesignSystem.ACCENT,WHITE=Color.WHITE,MUTED=CanvaDesignSystem.MUTED;','final int BG=CanvaDesignSystem.BG,PANEL=CanvaDesignSystem.PANEL,PANEL2=CanvaDesignSystem.PANEL_2,ACCENT=CanvaDesignSystem.ACCENT,PURPLE=CanvaDesignSystem.PURPLE,WHITE=Color.WHITE,MUTED=CanvaDesignSystem.MUTED;')
s=s.replace('v.getParent().getLayoutParams().width=dp(i.w);v.getParent().getLayoutParams().height=dp(i.h);v.getParent().requestLayout();','View parent=(View)v.getParent(); parent.getLayoutParams().width=dp(i.w); parent.getLayoutParams().height=dp(i.h); parent.requestLayout();')
p.write_text(s)
