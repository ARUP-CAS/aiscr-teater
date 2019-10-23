import React, { FC } from 'react'
import { LargeHeader } from '../../components/LargeHeader'
import { HeaderProps } from 'components/header/_types'

export const LargeHeaderContainer: FC<HeaderProps> = props => (
  <LargeHeader
    numberOfSearchResults={5}
    sliderImages={[
      `${process.env.PUBLIC_URL}/slider/1.jpg`, // eslint-disable-line no-undef
      `${process.env.PUBLIC_URL}/slider/2.jpg`, // eslint-disable-line no-undef
      `${process.env.PUBLIC_URL}/slider/3.jpg`, // eslint-disable-line no-undef
      `${process.env.PUBLIC_URL}/slider/4.jpg`, // eslint-disable-line no-undef
      `${process.env.PUBLIC_URL}/slider/5.jpg`, // eslint-disable-line no-undef
      `${process.env.PUBLIC_URL}/slider/6.jpg`, // eslint-disable-line no-undef
      `${process.env.PUBLIC_URL}/slider/7.jpg` // eslint-disable-line no-undef
    ]}
    hideDefaultBackgroundImage={true}
    {...props}
  />
)
