import React, { forwardRef, ReactNode } from 'react'
import 'scss/_markdown.scss'

// eslint-disable-next-line react/display-name
const Markdown = forwardRef<
  HTMLDivElement,
  {
    children?: ReactNode
    dangerouslySetInnerHTML?: {
      __html: string
    }
  }
>(({ children, dangerouslySetInnerHTML }, ref) => {
  return (
    <div
      className="markdown"
      ref={ref}
      dangerouslySetInnerHTML={dangerouslySetInnerHTML}
    >
      {children}
    </div>
  )
})

export { Markdown }
